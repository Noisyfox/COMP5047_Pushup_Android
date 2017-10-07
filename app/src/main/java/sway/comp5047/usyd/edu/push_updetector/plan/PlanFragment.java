package sway.comp5047.usyd.edu.push_updetector.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import sway.comp5047.usyd.edu.push_updetector.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment
{

	public PlanFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState )
	{
		// Inflate the layout for this fragment
		return inflater.inflate( R.layout.fragment_plan, container, false );
	}
}
