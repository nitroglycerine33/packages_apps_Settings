/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ViewFlipper;

import com.android.settings.R;

public class NavBar extends Fragment implements OnClickListener {

    private boolean mEditMode;
    private Button mBackButton, mNextButton;
    private Switch mSwitch;
    private ViewFlipper mViewFlipper;
    private final static Intent mIntent = new Intent("android.intent.action.NAVBAR_EDIT");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.nav_bar, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int padding = getActivity().getResources().getDimensionPixelSize(R.dimen.action_bar_switch_padding);
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM);
        mSwitch = new Switch(getActivity());
        mSwitch.setPadding(0, 0, padding, 0);
        getActivity().getActionBar().setCustomView(mSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.RIGHT));
        mViewFlipper = (ViewFlipper) getActivity().findViewById(R.id.view_flipper);
        mBackButton = (Button) getActivity().findViewById(R.id.back);
        mNextButton = (Button) getActivity().findViewById(R.id.next);
        mBackButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        updateButtons();
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditMode = isChecked;
                turnEditOn(mEditMode, true);
            }
        });
    }

    private void turnEditOn(boolean on, boolean save) {
        mIntent.putExtra("edit", on);
        mIntent.putExtra("save", save);
        getActivity().sendBroadcast(mIntent);
        mSwitch.setChecked(on);
    }

    @Override
    public void onPause() {
        turnEditOn(false, false);
        super.onPause();
    }

    @Override
    public void onStop() {
        turnEditOn(false, false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        turnEditOn(false, false);
        super.onDestroy();
    }

    protected void updateButtons() {
        int curViewIndex = mViewFlipper.indexOfChild(mViewFlipper.getCurrentView());
        mBackButton.setEnabled(curViewIndex != 0);
        mNextButton.setEnabled(curViewIndex != mViewFlipper.getChildCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v == mBackButton) {
            TranslateAnimation anim1 = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            TranslateAnimation anim2 = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            mViewFlipper.setInAnimation(anim2);
            anim2.setDuration(500);
            anim2.setInterpolator(new DecelerateInterpolator());
            anim1.setDuration(500);
            anim1.setInterpolator(new DecelerateInterpolator());
            mViewFlipper.setOutAnimation(anim1);
            mViewFlipper.showPrevious();
        } else if (v == mNextButton) {
            TranslateAnimation anim1 = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            TranslateAnimation anim2 = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            mViewFlipper.setInAnimation(anim2);
            anim2.setDuration(500);
            anim2.setInterpolator(new DecelerateInterpolator());
            anim1.setDuration(500);
            anim1.setInterpolator(new DecelerateInterpolator());
            mViewFlipper.setOutAnimation(anim1);
            mViewFlipper.showNext();
        }
        updateButtons();
    }
}
