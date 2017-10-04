package sway.comp5047.usyd.edu.push_updetector.devices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import sway.comp5047.usyd.edu.push_updetector.R;
import sway.comp5047.usyd.edu.push_updetector.devices.dummy.DummyContent;
import sway.comp5047.usyd.edu.push_updetector.devices.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceFragment extends Fragment
{

	// TODO: Customize parameter argument names
	private static final String ARG_COLUMN_COUNT = "column-count";
	// TODO: Customize parameters
	private int mColumnCount = 1;
	private OnListFragmentInteractionListener mListener;

	private RecyclerView list;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public DeviceFragment()
	{
	}

	// TODO: Customize parameter initialization
	@SuppressWarnings( "unused" )
	public static DeviceFragment newInstance( int columnCount )
	{
		DeviceFragment fragment = new DeviceFragment();
		Bundle args = new Bundle();
		args.putInt( ARG_COLUMN_COUNT, columnCount );
		fragment.setArguments( args );
		return fragment;
	}

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		if( getArguments()!=null )
		{
			mColumnCount = getArguments().getInt( ARG_COLUMN_COUNT );
		}
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState )
	{
		View view = inflater.inflate( R.layout.fragment_device_list, container, false );

		list = view.findViewById( R.id.list );
		// Set the adapter
		Context context = view.getContext();
		if( mColumnCount<=1 )
		{
			list.setLayoutManager( new LinearLayoutManager( context ) );
		}
		else
		{
			list.setLayoutManager( new GridLayoutManager( context, mColumnCount ) );
		}
		list.setAdapter( new MyDeviceRecyclerViewAdapter( DummyContent.ITEMS, mListener ) );

		view.findViewById( R.id.add_device ).setOnClickListener( v ->
		{

		} );

		return view;
	}


	@Override
	public void onAttach( Context context )
	{
		super.onAttach( context );
		if( context instanceof OnListFragmentInteractionListener )
		{
			mListener = ( OnListFragmentInteractionListener ) context;
		}
		else
		{
			throw new RuntimeException( context.toString()
					+ " must implement OnListFragmentInteractionListener" );
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mListener = null;
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
	public interface OnListFragmentInteractionListener
	{
		// TODO: Update argument type and name
		void onListFragmentInteraction( DummyItem item );
	}
}
