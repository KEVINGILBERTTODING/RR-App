package com.example.rumahraga.ui.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentEventsFragmentsBinding;
import com.example.rumahraga.model.EventModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.event.EventAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.viewmodel.event.EventViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class EventsFragments extends Fragment implements ItemClickListener {
    private EventViewModel eventViewModel;
    private FragmentEventsFragmentsBinding binding;
    private List<EventModel> eventModelList;
    private EventAdapter eventAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventsFragmentsBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEvents();
        listener();
    }

    private void init() {
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }



    private void listener() {


        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }


    private void getEvents() {
        binding.rvEvent.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.tvEmpty.setVisibility(View.GONE);
        eventViewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<EventModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<EventModel>> listResponseModel) {
                binding.rvEvent.setVisibility(View.VISIBLE);
                binding.shimmer.setVisibility(View.GONE);
                if (listResponseModel.isStatus() == true) {
                    eventModelList = listResponseModel.getData();
                    eventAdapter = new EventAdapter(getContext(), eventModelList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvEvent.setLayoutManager(linearLayoutManager);
                    binding.rvEvent.setAdapter(eventAdapter);
                    binding.rvEvent.setHasFixedSize(true);
                    eventAdapter.setItemClickListener(EventsFragments.this);

                    binding.tvEmpty.setVisibility(View.GONE);


                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setText("Tidak ada event");
                    showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());

                }
            }
        });
    }

    private void showToast(String type, String message) {
        if (type.equals(ConsOther.TOAST_SUCCESS)) {
            Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else if (type.equals(ConsOther.TOAST_NORMAL)){
            Toasty.normal(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();

        }
    }

    private void filter(String newText) {
        ArrayList<EventModel> filteredList = new ArrayList<>();
        for (EventModel item : eventModelList) {
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            eventAdapter.filter(filteredList);
            if (!filteredList.isEmpty()) {
                binding.tvEmpty.setVisibility(View.GONE);
                eventAdapter.filter(filteredList);
            }else{
                binding.tvEmpty.setText("Tidak ada event");
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }


        }

    }


    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        EventModel eventModel = (EventModel)  object;
        if (eventModel != null) {
            Fragment fragment = new EventDetailFragment();
            Bundle arg = new Bundle();
            arg.putInt("category_id", eventModel.getCategory_id());
            arg.putString("event_name", eventModel.getTitle());
            arg.putString("date", eventModel.getDate());
            arg.putString("category_name", eventModel.getEvent_category());
            arg.putString("content", eventModel.getContent());
            arg.putString("image", eventModel.getPicture_name());
            fragment.setArguments(arg);
            fragmentTransaction(fragment);



        }else {

        }
    }


}