package com.software.reuze;
import java.util.List;

/**
 * Stores mocap animation data.
 * 
 * @author Michael Kipp
 */
public class vam_BonesData
{

    public float _data[][]; // first index: bones, second index: frames
    public int _numFrames;
    public float _fps;

    public vam_BonesData(int numBones)
    {
        _data = new float[numBones][];
    }

    public void putBoneData(int index, List<Float> data)
    {
        _data[index] = new float[data.size()];
        int j = 0;
        for (Float x : data) {
            _data[index][j++] = x;
        }
    }

    public void putBoneData(int index, float[] data)
    {
        _data[index] = data;
    }

    public float[] getBoneData(int index)
    {
        return _data[index];
    }

    public void setNumFrames(int n)
    {
        _numFrames = n;
    }

    public int getNumFrames()
    {
        return _numFrames;
    }

    public void setFps(float fps)
    {
        _fps = fps;
    }

    public float getFps()
    {
        return _fps;
    }

    @Override
    public String toString()
    {
        return "<AnimData frames:" + _numFrames + ">";
    }
}
