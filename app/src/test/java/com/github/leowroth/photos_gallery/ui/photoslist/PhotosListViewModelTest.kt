package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.usecase.GetPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.InsertAllPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.RefreshPhotosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class PhotosListViewModelTest {

    @get:Rule
    var coroutinesTestRule: TestRule = InstantTaskExecutorRule()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val getPhotosUseCase = mockk<GetPhotosUseCase>()
    private val refreshPhotosUseCase = mockk<RefreshPhotosUseCase>()
    private val insertAllPhotosUseCase = mockk<InsertAllPhotosUseCase>()

    private lateinit var photosListViewModel: PhotosListViewModel

    @Before
    fun setup() {

        Dispatchers.setMain(mainThreadSurrogate)

        coEvery {
            refreshPhotosUseCase.invoke()
        } answers { nothing }

        val expectedPhotos = liveData<MutableList<Photo>> {
            mutableListOf(
                Photo(id = "First"), Photo(id = "Second")
            )
        }
        coEvery { getPhotosUseCase.invoke() } answers { expectedPhotos }

        coEvery { insertAllPhotosUseCase.insertAll(any()) } answers { nothing }

        photosListViewModel =
            PhotosListViewModel(
                getPhotosUseCase,
                refreshPhotosUseCase,
                insertAllPhotosUseCase,
                mainThreadSurrogate,
                mainThreadSurrogate
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `eventNetworkError is true when IOException`() =
        runBlockingTest {
            val expectedPhotos = mutableListOf(
                Photo(id = "First"), Photo(id = "Second")
            )

            coEvery { getPhotosUseCase.invoke().value } answers { expectedPhotos }

            coEvery {
                refreshPhotosUseCase.invoke()
            } throws IOException()

            var result = false

            photosListViewModel.eventNetworkErrorData.observeForever {
                result = it
            }
            photosListViewModel.forceRefreshDataFromRepository()

            assertEquals(true, result)
            /* TODO somehow the assertEquals is still run before the
            viewModelScope.launch() in PhotosListViewModel:44
             */
        }


    //TODO Has problems with LiveData as well
    @Test
    fun `onPhotoClicked calls insertAllPhotosUseCaseImpl clicked Photo at position 0`() =
        runBlockingTest {
            val currentPosition = 1
            val currentPhoto = Photo(id = "Second")
            val currentPhotos = mutableListOf(
                Photo(id = "First"),
                currentPhoto,
                Photo(id = "Third"),
                Photo(id = "Fourth")
            )
            val currentPhotosList =
                liveData<MutableList<Photo>> { currentPhotos }
            coEvery { getPhotosUseCase.invoke() } answers { currentPhotosList }

            photosListViewModel.onPhotoClicked(currentPosition)

            coVerify { insertAllPhotosUseCase.insertAll(currentPhotos) }
        }
}