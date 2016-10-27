package com.example.cc.gogo.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.MenuItem;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.cc.gogo.Fragments.InputDataFragment;
import com.example.cc.gogo.R;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(new EmptyPagerAdapter(getSupportFragmentManager()));

        StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);
        assert indicator != null;
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, true);

        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                pager.setCurrentItem(step, true);
            }
        });
    }

    public static class PageFragment extends Fragment {

        private TextView lblPage;

        public static PageFragment newInstance(int page, boolean isLast) {
            Bundle args = new Bundle();
            args.putInt("page", page);
            if (isLast)
                args.putBoolean("isLast", true);
            final PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_page, container, false);
            lblPage = (TextView) view.findViewById(R.id.lbl_page);
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final int page = getArguments().getInt("page", 0);
            if (getArguments().containsKey("isLast")) {
                lblPage.setText("You're done!");
            }
            else {
                lblPage.setText(Integer.toString(page));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                StepsActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class EmptyPagerAdapter extends FragmentPagerAdapter {

        public EmptyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0)
            {
                return new InputDataFragment();
            }
            return PageFragment.newInstance(position + 1, position == getCount() - 1);
        }

    }
}
