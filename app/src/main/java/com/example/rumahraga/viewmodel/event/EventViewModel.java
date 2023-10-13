package com.example.rumahraga.viewmodel.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.event.EventRepository;
import com.example.rumahraga.model.EventModel;
import com.example.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {

    private EventRepository eventRepository;

    @Inject
    public EventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public LiveData<ResponseModel<List<EventModel>>> getEvent() {
        return eventRepository.getEvents();
    }
}
