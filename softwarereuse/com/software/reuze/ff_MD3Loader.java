package com.software.reuze;

public class ff_MD3Loader {
	String strModel;
	Object m_head;
	public ff_MD3Model loadModel(String directoryPath) {
		String strLowerModel;	// This stores the file name for the lower.md3 model
		String strUpperModel;	// This stores the file name for the upper.md3 model
		String strHeadModel;	// This stores the file name for the head.md3 model
		String strLowerSkin;	// This stores the file name for the lower.md3 skin
		String strUpperSkin;	// This stores the file name for the upper.md3 skin
		String strHeadSkin;	    // This stores the file name for the head.md3 skin
		ff_MD3LoadFile loadMd3=new ff_MD3LoadFile();	    // This object allows us to load each .md3 and .skin file
		
		// Make sure valid path and model names were passed in
		if(directoryPath==null) return null;
		int i=directoryPath.lastIndexOf('/');
		if (i<0) i=directoryPath.lastIndexOf('\\');
		strModel = directoryPath.substring(i+1);

		// Store the correct files names for the .md3 and .skin file for each body part.
		// We concatenate this on top of the path name to be loaded from.
		strLowerModel = directoryPath+'/'+strModel+"_lower.md3";
		strUpperModel = directoryPath+'/'+strModel+"_upper.md3";
		strHeadModel = directoryPath+'/'+strModel+"_head.md3";
		
		// Get the skin file names with their path
		strLowerSkin = directoryPath+'/'+strModel+"_lower.skin";
		strUpperSkin = directoryPath+'/'+strModel+"_upper.skin";
		strHeadSkin = directoryPath+'/'+strModel+"_head.skin";
		
		// Load the head mesh (*_head.md3) and make sure it loaded properly
		m_head = loadMd3.ImportMD3(strHeadModel);
		if (m_head==null) return null;
/*
		// Load the upper mesh (*_head.md3) and make sure it loaded properly
		if(!loadMd3.ImportMD3(&m_Upper, strUpperModel))		
		{
			sprintf(strUpperModel, "%s/upper.md3", strPath, strModel); clear(&m_Upper);
			if(!loadMd3.ImportMD3(&m_Upper, strUpperModel)) return false;
		}

		// Load the lower mesh (*_lower.md3) and make sure it loaded properly
		if(!loadMd3.ImportMD3(&m_Lower, strLowerModel))
		{
			sprintf(strLowerModel, "%s/lower.md3", strPath, strModel); clear(&m_Lower);
			if(!loadMd3.ImportMD3(&m_Lower, strLowerModel)) return false;
		}

		// Load the lower skin (*_upper.skin) and make sure it loaded properly
		if(!loadMd3.LoadSkin(&m_Lower, strLowerSkin))
		{
			sprintf(strLowerSkin, "%s/lower_default.skin", strPath, strModel);
			if(!loadMd3.LoadSkin(&m_Lower, strLowerSkin)) return false;
		}

		// Load the upper skin (*_upper.skin) and make sure it loaded properly
		if(!loadMd3.LoadSkin(&m_Upper, strUpperSkin))
		{
			sprintf(strUpperSkin, "%s/upper_default.skin", strPath, strModel);
			if(!loadMd3.LoadSkin(&m_Upper, strUpperSkin)) return false;
		}

		// Load the head skin (*_head.skin) and make sure it loaded properly
		if(!loadMd3.LoadSkin(&m_Head,  strHeadSkin))
		{
			sprintf(strHeadSkin,  "%s/head_default.skin",  strPath, strModel);
			if(!loadMd3.LoadSkin(&m_Head,  strHeadSkin)) return false;
		}

		// Load the lower, upper and head textures.  
		LoadModelTextures(&m_Lower, strPath);
		LoadModelTextures(&m_Upper, strPath);
		LoadModelTextures(&m_Head,  strPath);

		// We added to this function the code that loads the animation config file

		// This stores the file name for the .cfg animation file
		char strConfigFile[255] = {0};	

		// Add the path and file name prefix to the animation.cfg file
		sprintf(strConfigFile,  "%s/%s_animation.cfg",  strPath, strModel);

		// Load the animation config file (*_animation.config) and make sure it loaded properly
		if(!LoadAnimations(strConfigFile))
		{
			sprintf(strConfigFile,  "%s/animation.cfg",  strPath, strModel);
			if(!LoadAnimations(strConfigFile)) return false;
		}

		// Link the lower body to the upper body when the tag "tag_torso" is found in our tag array
		LinkModel(&m_Lower, &m_Upper, "tag_torso");

		// Link the upper body to the head when the tag "tag_head" is found in our tag array
		LinkModel(&m_Upper, &m_Head, "tag_head");
			
		// The character was loaded correctly so return true, the weapon is optional
		LoadWeapon(strPath, "weapon");
		i=getMax(0,&m_Head);
		i=getMax(i,&m_Upper);
		i=getMax(i,&m_Lower);
		numberOfFrames=getMax(i,&m_Weapon);
*/
		return null;
	}
	public static void main(String args[]) {
		ff_MD3Model model;
		model = new ff_MD3Loader().loadModel("data/elf");
		System.out.println(model);
	}

}
//********************************************************************************//
////
//- "Talk to me like I'm a 3 year old!" Programming Lessons -				  //
////
//$Author:		DigiBen		digiben@gametutorials.com					  //
////
//$Program:		MD3 Animation											  //
////
//$Description:	Demonstrates animating Quake3 characters with quaternions //
////
//********************************************************************************//
//This tutorial is the second MD3 tutorial.  In this version we will be able to load and
//animate the Quake3 character using quaternions.    Be sure you have read the MD3 Loader
//and Quaternions tutorial on our site at www.GameTutorials.com in the OpenGL section.
//This tutorial was written assuming you did, which enables us to cut out a ton of comments
//and explanations that can be found in the previous tutorials.  I will leave some comments
//to refresh your memory, but most of the large blocks will be taken out.
//
//Okay, so what's different from this tutorial and the last one?  Well, if you run the
//program you will see that our character is animating.  If you click the right and let
//mouse buttons you can cycle through the torso and leg animations.  The name of the
//animations are also displayed in the title bar.  So the question is, how much of the
//code changed to make this happen?  Unfortunately, it's just a couple simple additions,
//but it isn't a bunch of complicated ones either.  The only complicated code is the
//quaternion code.  As I explained in the first quaternions tutorial, the math is quite
//complex to wrap your head around.  Don't stress out about trying to understand why it works,
//it's sufficient enough to understand the equations needed to perform the desired task.
//It goes without saying that if you do have a desire to delve into the math and proofs,
//you will be a better person for it, for it can only help you.  I will NOT be going over
//the theory or proofs beneath the quaternion equations.  Just get a math book.  I will
//talk more about the quaternions in Md3.cpp.  Here is a address to a really detailed PDF 
//file written by Ken Shoemake on quaternions:
//
//http://www.daimi.au.dk/~mbl/cgcourse/wiki/_files/shoemake94.pdf
//
//As well as another great PDF from Magic Software here:
//
//http://www.magic-software.com/Documentation/quat.pdf
//
//One of the most notable changes added to this tutorial is that we now read in the
//animation config file, with the .cfg extension.  Remember from the last tutorial that
//only the torso (upper) and the legs (lower) have animation associated with them.  If you
//want the head to animate, the artist will need to make the head part of the upper.md3 file.
//This is why on some models you will see a small head when you turn on wire frame mode.
//The config file stores all of the animations and their respective frame data.  For example, 
//here is a few lines from a config file:
//
//		0	31	0	25		// BOTH_DEATH1
//		31	1	1	25		// BOTH_DEAD1
//		32	35	0	20		// BOTH_DEATH2
//
//These 3 lines indicate 3 animations that both the torso and legs must perform.
//The four numbers in front of the animation name store the starting frame, the ending
//frame, the number of looping frames (ignored) and the frames per second the animation runs.
//In the upper.md3 and the lower.md3, vertex key frames are saved, almost exactly like the
//.MD2 file format is saved.  The first 2 numbers are indices into those key frames, which
//are loading with the vertex array.  They aren't actually indexed directly, but depending
//on the current key frame, you would say:
//
//currentIndex = pModel->currentFrame * pObject->numOfVerts;
//
//Since all the key frames are stored in a 1 dimensional array, we treat it like a 2D
//array by multiplying the current frame by the number of vertices of the model (which
//does not count the key frames, just vertex count for one frame).  That new index is
//used to index into the vertex array of the current model.
//
//There are 3 blocks of animations in the config file.  They are BOTH_*, TORSO_* and LEGS_*.
//This is because the torso model can be doing a different animation that has nothing to do
//with the legs animation.  For instance, the character can be doing a shooting animation,
//while they are jumping, landing, turning, swimming, etc...
//
//It is assumed that by reading this tutorial you understand a decent amount of OpenGL and
//are comfortable with advanced topics.  This is a extremely advanced topic and should not
//be tackled until you get to a certain level of understanding in 3D.  I would recommend
//people who might not be up to that level to try looking at the MD2 Loader and animation.
//This is still an advanced tutorial, but not as complicated.  It only gets harder from here.
//And remember, copy and pasting of code hurts small children in Africa :)
//
//The last notable change from the last tutorial is that we now use a matrix to perform the
//translations and rotations of our model's body parts for animation.  Since we are using 
//quaternions, and the fact that the key frame rotations are stored in a 3x3 matrix. this 
//is necessary.  The rest of the functions and things added are just smaller helper functions.

///////////////////////////////////////////////////////////////////////////////////
//
//This version of the tutorial incorporates the animation data stored in the MD3
//character files.  We will be reading in the .cfg file that stores the animation
//data.  The rotations and translations of the models will be done using a matrix.
//There will be no more calls to glTranslatef().  To create the rotation and
//translation matrix, quaternions will be used.  This is because quaternions
//are excellent for interpolating between 2 rotations, as well as not overriding
//another translation causing "gimbal lock".
//
//So, why do we need to interpolate?  Well, the animations for the character are
//stored in key frames.  Instead of saving each frame of an animation, key frames
//are stored to cut down on memory and disk space.  The files would be huge if every
//frame was saved for every animation, as well as creating a huge memory footprint.  
//Can you imagine having 10+ models in memory with all of that animation data?  
//
//The animation key frames are stored in 2 ways.  The torso and legs mesh have vertices
//stored for each of the key frames, along with separate rotations and translations
//for the basic bone animation.  Remember, that each .md3 represents a bone, that needs
//to be connected at a joint.  For instance, the torso is connected to the legs, and the
//head is connected to the torso.  So, that makes 3 bones and 2 joints.  If you add the
//weapon, the weapon is connected to the hand joint, which gives us 4 bones and 3 joints.
//Unlike conventional skeletal animation systems, the main animations of the character's
//movement, such as a gesture or swimming animation, are done not with bones, but with 
//vertex key frames, like in the .md2 format. Since the lower, upper, head and weapon models
//are totally different models, which aren't seamlessly connected to each other, the parent
//node needs to end a message (a translation and rotation) down to all it's child nodes to
//tell them where they need to be in order for the animation to look right.  A good example
//of this is when the legs have the back flip animation.  The legs might kick back into a back
//flip that lands the character on their feet.  Well, since the main models are separate,
//if the legs didn't tell the torso where to go, then the model's torso would stay in the same
//place and the body would detach itself from the legs.  The exporter calculates all this stuff
//for you of course.
//
//But getting back to the interpolation, since we use key frames, if we didn't interpolate
//between them, the animation would look very jumpy and unnatural.  It would also go too
//fast.  By interpolating, we create a smooth transition between each key frame.
//
//As seen in the .md2 tutorials, interpolating between vertices is easy if we use the
//linear interpolation function:  p(t) = p0 + t(p1 - p0).  The same goes for translations,
//since it's just 2 3D points.  This is not so for the rotations.  The Quake3 character
//stores the rotations for each key frame in a 3x3 matrix.  This isn't a simple linear
//interpolation that needs to be performed.  If we convert the matrices to a quaternion,  
//then use spherical linear interpolation (SLERP) between the current frame's quaternion 
//and the next key frame's quaternion, we will have a new interpolated quaternion that
//can be converted into a 4x4 matrix to be applied to the current model view matrix in OpenGL.
//After finding the interpolated translation to be applied, we can slip that into the rotation
//matrix before it's applied to the current matrix, which will require only one matrix command.
//
//You'll notice that in the CreateFromMatrix() function in our quaternion class, I allow a
//row and column count to be passed in.  This is just a dirty way to allow a 3x3 or 4x4 matrix
//to be passed in.  Instead of creating a whole new function and copy and pasting the main 
//code, it seemed fitting for a tutorial.  It's obvious that the quaternion class is missing
//a tremendous amount of functions, but I chose to only keep the functions that we would use.
//
//For those of you who don't know what interpolation us, here is a section abstracted 
//from the MD2 Animation tutorial:
//
//-------------------------------------------------------------------------------------
//Interpolation: Gamedev.net's Game Dictionary say interpolation is "using a ratio 
//to step gradually a variable from one value to another."  In our case, this
//means that we gradually move our vertices from one key frame to another key frame.
//There are many types of interpolation, but we are just going to use linear.
//The equation for linear interpolation is this:
//
//				p(t) = p0 + t(p1 - p0)
//
//				t - The current time with 0 being the start and 1 being the end
//				p(t) - The result of the equation with time t
//				p0 - The starting position
//				p1 - The ending position
//
//Let's throw in an example with numbers to test this equation.  If we have
//a vertex stored at 0 along the X axis and we wanted to move the point to
//10 with 5 steps, see if you can fill in the equation without a time just yet.
//
//Finished?  You should have come up with:
//
//				p(t) = 0 + t(10 - 0)
//				p(t) = 0 + 10t
//				p(t) = 10t
//
//Now, all we need it a time from 0 to 1 to pass in, which will allow us to find any
//point from 0 to 10, depending on the time.  Since we wanted to find out the distance
//we need to travel each frame if we want to reach the end point in 5 steps, we just
//divide 1 by 5: 1/5 = 0.2
//
//We can then pass this into our equation:
//
//				p(0.2) = 10 * 0.2
//				p(0.2) = 2
//
//What does that tell us?  It tells us we need to move the vertex along the x
//axis each frame by a distance of 2 to reach 10 in 5 steps.  Yah, yah, this isn't
//rocket science, but it's important to know that what your mind would have done
//immediately without thinking about it, is linear interpolation.  
//
//Are you starting to see how this applies to our model?  If we only read in key
//frames, then we need to interpolate every vertex between the current and next
//key frame for animation.  To get a perfect idea of what is going on, try
//taking out the interpolation and just render the key frames.  You will notice
//that you can still see what is kinda going on, but it moves at an incredible pace!
//There is not smoothness, just a really fast jumpy animation.
//------------------------------------------------------------------------------------
//
/////////////////////////////////////////////////////////////////////////////////
//
// * QUICK NOTES * 
//
// 
// Below I will sum up everything that we went over in this tutorial.  I don't 
// think it was a ton of things to sift through, but certainly model loading and
// animation are huge subjects that need a lot of code.  You can't just call
// glLoadModel() and glAnimateModel() for this stuff :)
// 
// First of all, we added a basic quaternion class to our tutorial.  This is used
// to take a matrix, convert it to a quaternion, interpolate between another 
// quaternion that was converted to a matrix, then turned back into a matrix.
// This is because quaternions are a great way to interpolate between rotations.
// If you wanted to use the glRotatef() and glTranslatef() functions, you would
// want to convert the interpolated quaternion to an axis angle representation,
// which might be less code, but not as useful in the long run.
// 
// The next important thing that was added was the interpolation between vertex
// key frames.  We learned earlier (top of Md3.cpp) that not most of the animation
// is done through key frame vertices, not bones.  The only bone animation that is
// done is with the joints that connect the .md3 models together.  The game Half-Life, 
// for example, uses a full on skeletal animation.
// 
// Also, in this tutorial we added the code to parse the animation config file (.cfg).
// This stores the animation information for each animation.  The order of them are
// important.  For the most part, the config files are the same format.  As discussed
// in the previous MD3 tutorial, there is a few extra things in the config file that
// we don't read in here, such as footstep sounds and initial positions.  The tutorial
// was not designed to be a robust reusable Quake3 character loader, but to teach you
// how it works for the most part.  Other things are extra credit :)
//
// Another important thing was our timing system.  Since we were dealing with multiple
// models that had different frames per second, we needed to add some variables to our
// t3DModel class to hold some things like elapsedTime and the current t value.  This
// way, no static variables had to be created, like in the .MD2 tutorial.
// 
// Let me know if this helps you out!
// 
// The Quake3 .Md3 file format is owned by ID Software.  This tutorial is being used 
// as a teaching tool to help understand model loading and animation.  This should
// not be sold or used under any way for commercial use with out written consent
// from ID Software.
//
// Quake, Quake2 and Quake3 are trademarks of ID Software.
// All trademarks used are properties of their respective owners. 
//
//
// C 2000-2005 GameTutorials
