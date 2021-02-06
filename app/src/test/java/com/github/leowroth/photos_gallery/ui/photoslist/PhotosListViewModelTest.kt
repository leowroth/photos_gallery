package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.lifecycle.MutableLiveData
import com.github.leowroth.photos_gallery.CoroutinesTestRule
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.usecase.GetPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.InsertAllPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.RefreshPhotosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class PhotosListViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val getPhotosUseCase = mockk<GetPhotosUseCase>()
    private val refreshPhotosUseCase = mockk<RefreshPhotosUseCase>()
    private val insertAllPhotosUseCase = mockk<InsertAllPhotosUseCase>()

    private lateinit var photosListViewModel: PhotosListViewModel


    @Before
    fun setup() {
        coEvery {
            refreshPhotosUseCase.invoke()
        } answers { nothing }

    }


    @Test
    fun `viewModel calls refreshPhotosUseCase at initiation`() =
        runBlockingTest {
            val expectedPhotos = MutableLiveData(
                mutableListOf(
                    Photo(id = "First"), Photo(id = "Second")
                )
            )
            coEvery { getPhotosUseCase.invoke() } answers { expectedPhotos }

            photosListViewModel =
                PhotosListViewModel(
                    getPhotosUseCase,
                    refreshPhotosUseCase,
                    insertAllPhotosUseCase
                )
            photosListViewModel.forceRefreshDataFromRepository()

            assertEquals(false, photosListViewModel.eventNetworkErrorData.value)
            assertEquals(expectedPhotos, photosListViewModel.photosList)
            coVerify { refreshPhotosUseCase.invoke() }
        }

    @Test
    fun `eventNetworkError is true when IOException`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            val expectedPhotos = MutableLiveData(
                mutableListOf(
                    Photo(id = "First"), Photo(id = "Second")
                )
            )
            coEvery { getPhotosUseCase.invoke() } answers { expectedPhotos }

            coEvery {
                refreshPhotosUseCase.invoke()
            } throws IOException()

            photosListViewModel =
                PhotosListViewModel(
                    getPhotosUseCase,
                    refreshPhotosUseCase,
                    insertAllPhotosUseCase
                )
            photosListViewModel.forceRefreshDataFromRepository()

            assertEquals(true, photosListViewModel.eventNetworkErrorData.value)
        }

    @Test
    fun `onFavedClicked calls photoFavedUseCase with correct Photo`() =
        runBlockingTest {
            val currentPosition = 1
            val currentPhoto = Photo(id = "Second")
            val currentPhotosList = MutableLiveData(
                mutableListOf(
                    Photo(id = "First"),
                    currentPhoto,
                    Photo(id = "Third"),
                    Photo(id = "Fourth")
                )
            )
            coEvery { getPhotosUseCase.invoke() } answers { currentPhotosList }

            photosListViewModel =
                PhotosListViewModel(
                    getPhotosUseCase,
                    refreshPhotosUseCase,
                    insertAllPhotosUseCase
                )

            photosListViewModel.onPhotoClicked(currentPosition)

//            coVerify {
////                insertAllPhotosUseCase.insertAll(currentPhoto)
//            }
        }
}