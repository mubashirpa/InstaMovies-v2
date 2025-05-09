package app.instamovies.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.domain.model.person.popular.PersonResultModel
import app.instamovies.domain.usecase.person.GetPopularPersonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
    @Inject
    constructor(
        private val getPopularPersonUseCase: GetPopularPersonUseCase,
    ) : ViewModel() {
        private val _personStateFlow: MutableStateFlow<PagingData<PersonResultModel>> =
            MutableStateFlow(PagingData.empty())
        val personStateFlow: MutableStateFlow<PagingData<PersonResultModel>> get() = _personStateFlow

        init {
            getPopularPerson()
        }

        private fun getPopularPerson() {
            viewModelScope.launch {
                getPopularPersonUseCase()
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        _personStateFlow.value = it
                    }
            }
        }
    }
