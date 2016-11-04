package com.example.cc.gogo.Activities;

import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import android.app.FragmentManager;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.support.annotation.*;

import com.example.cc.gogo.Fragments.*;
import com.example.cc.gogo.R;
import com.lovearthstudio.duasdk.Dua;
import com.lovearthstudio.duaui.DuaActivityLogin;
import com.roughike.bottombar.*;


import libsvm.svm;

import com.example.cc.gogo.svm.*;

import static com.example.cc.gogo.util.Constant.modelFileBackupName;
import static java.io.File.separator;
import static com.example.cc.gogo.svm.SVM.inputStreamToArray;
import static com.example.cc.gogo.util.Constant.dir;
import static com.example.cc.gogo.util.Constant.modelFileName;
import static com.example.cc.gogo.util.Constant.rangeFileName;
import static com.example.cc.gogo.util.Constant.trainStillFileName;
import static com.example.cc.gogo.util.Constant.train;
import static com.example.cc.gogo.util.Constant.trainFileName;
import static com.example.cc.gogo.util.PermissionUtil.requestWriteFilePermission;


public class MainActivity extends AppCompatActivity {
    BottomBar bottomBar;
    SVM mSvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectPager();
        setContentView(com.example.cc.gogo.R.layout.main_activity);
        requestWriteFilePermission(this);
        init();
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CollectionFragment());
        fragmentList.add(new RunFragment());
        fragmentList.add(new SettingFragment());
        fragmentList.add(new AdviceFragment());
        /*//initial fragment show
        *//*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CollectionFragment cf = new CollectionFragment();
        //fragmentTransaction.replace(R.id.contentContainer,cf);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commit();*//*
        //bottom show*/
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (tabId) {
                    case R.id.home:
                        //CollectionFragment cf = new CollectionFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragmentList.get(0));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.run:
                        //RunFragment rf = new RunFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragmentList.get(1));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.setting:
                        //SettingFragment sf = new SettingFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragmentList.get(2));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    case R.id.advice:
                        //SettingFragment sf = new SettingFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragmentList.get(3));
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 初始化操作
     */
    private void init() {
        crateDataDir();
        copyFileToSd();
        loadModelAndRange();
    }

    private void selectPager() {
        Dua.DuaUser duaUser = Dua.getInstance().getCurrentDuaUser();
        if (duaUser.logon) {
            return;
        } else {
            startActivityForResult(new Intent(this, DuaActivityLogin.class), 10086);
        }
    }


    /**
     * copy model 和 range文件到sd卡
     */
    private void copyFileToSd() {
        try {
            copyFileToSd(getAssets().open("model"), dir + separator + modelFileName);
            copyFileToSd(getAssets().open("range"), dir + separator + rangeFileName);
            copyFileToSd(getAssets().open("train"), dir + separator + trainFileName);
            copyFileToSd(getAssets().open("train_still"), dir + separator + trainStillFileName);
            copyFileToSd(getAssets().open("model_backup"), dir + separator + modelFileBackupName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copy文件到sd卡
     */
    private void copyFileToSd(InputStream in, String targetFilePath) {
        FileOutputStream fileOutputStream = null;
        File file = new File(targetFilePath);
        if (file.exists()) {        // 如果文件已经存在就结束
            return;
        }
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(targetFilePath);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建数据目录
     */
    private void crateDataDir() {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File trainFile = new File(dir + separator + train);
        if (!trainFile.exists()) {
            trainFile.mkdirs();
        }
    }



    /**
     * 加载model和range
     */
    private void loadModelAndRange() {
        try {
            mSvm = new SVM(svm.svm_load_model(
                    new BufferedReader(new InputStreamReader(new FileInputStream(dir + separator + modelFileName)))),
                    inputStreamToArray(new FileInputStream(dir + separator + rangeFileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean predictUnscaledTrain(String[] unScaleData) {
        return mSvm.predictUnscaledTrain(unScaleData);
    }

    public double predictUnscaled(String[] unScaleData) {
        return mSvm.predictUnscaled(unScaleData, false);
    }
}