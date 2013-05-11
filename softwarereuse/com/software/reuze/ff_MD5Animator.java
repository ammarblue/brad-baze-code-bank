package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


/** An animation controller for MD5 (Doom 3) animations.
 * 
 * @author Dave Clayton <contact@redskyforge.com> */
public class ff_MD5Animator extends va_a_AnimationController {

	protected ff_MD5Joints mCurrentFrame = null;
	protected ff_MD5Joints mNextFrame = null;
	protected ff_MD5Joints mSkeleton = null;

	/** Set the current skeleton.
	 * @param skeleton */
	public void setSkeleton (ff_MD5Joints skeleton) {
		mSkeleton = skeleton;
	}

	/** Get the current skeleton.
	 * @return the skeleton. */
	public ff_MD5Joints getSkeleton () {
		return mSkeleton;
	}

	@Override
	/**
	 * Sets the currently playing {@link MD5Animation}.
	 * @param anim
	 *          An {@link MD5Animation}.
	 * @param WrapMode
	 *          The animation {@link WrapMode}.
	 */
	public void setAnimation (va_a_Animation anim, WrapMode wrapMode) {
		super.setAnimation(anim, wrapMode);

		if (anim != null) {
			mCurrentFrame = mSkeleton = mNextFrame = ((ff_MD5Animation)anim).frames[0];
		}
	}

	@Override
	protected void setInterpolationFrames () {
		mCurrentFrame = ((ff_MD5Animation)mCurrentAnim).frames[mCurrentFrameIdx];
		mNextFrame = ((ff_MD5Animation)mCurrentAnim).frames[mNextFrameIdx];
	}

	@Override
	protected void interpolate () {
		ff_MD5Animation.interpolate(mCurrentFrame, mNextFrame, mSkeleton, mFrameDelta);
	}
}
