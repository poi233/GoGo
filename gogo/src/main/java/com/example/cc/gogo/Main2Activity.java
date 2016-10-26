package com.example.cc.gogo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.contentContainer)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentContainer, StepFragment.newInstance())
                    .commit();
        }
    }

    @OnClick(R.id.home)
    public void onClickNavigation1() {
        transaction(CollectionFragment.newInstance());
    }

    @OnClick(R.id.sport)
    public void onClickNavigation2() {
        transaction(CollectionFragment.newInstance());
    }

    @OnClick(R.id.run)
    public void onClickNavigation3() {
        transaction(CollectionFragment.newInstance());
    }

    @OnClick(R.id.music)
    public void onClickNavigation4() {
        transaction(CollectionFragment.newInstance());
    }

    @OnClick(R.id.setting)
    public void onClickNavigation5() {
        transaction(CollectionFragment.newInstance());
    }

    private void transaction(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contentContainer, fragment)
                .commit();
    }
}
