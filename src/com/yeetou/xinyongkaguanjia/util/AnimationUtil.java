package com.yeetou.xinyongkaguanjia.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimationUtil {

	public static void setRoundAtimation(View v) {

		Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setDuration(1000);
		v.startAnimation(rotateAnimation);

	}

}
