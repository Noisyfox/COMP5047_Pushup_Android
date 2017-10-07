package sway.comp5047.usyd.edu.push_updetector.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sway.comp5047.usyd.edu.push_updetector.R;
import sway.comp5047.usyd.edu.push_updetector.devices.dummy.DummyContent;
import sway.comp5047.usyd.edu.push_updetector.devices.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceFragment extends Fragment {
    private RecyclerView list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        list = view.findViewById(R.id.list);
        // Set the adapter
        Context context = view.getContext();
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setAdapter(new MyDeviceRecyclerViewAdapter(DummyContent.ITEMS, i -> {

        }));

        view.findViewById(R.id.add_device).setOnClickListener(v ->
        {
            getActivity().startActivity(new Intent(getContext(), WifiSelectionActivity.class));
        });

        return view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
