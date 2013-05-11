package com.software.reuze;

public class gb_TerrainSunLighting
{
	gb_Vector3 _sd;
	gb_Vector3 _sc;
	gb_Vector3 _am;
	gb_Vector3 skytop;
	gb_Vector3 skymid;
	gb_Vector3 skybot;
	gb_Vector3 fogcol;
	float   shadowDist;
	boolean useClouds;
	boolean useExpensiveAO;
	public static final float LightAngleRatio = (float) Math.tan(Math.toRadians(4));
	public static final gb_Vector3 SKY1=new gb_Vector3(1,0.3f,0.05f);
	public static final gb_Vector3 SKY2=new gb_Vector3(1,0.8f,0.4f);
	public static final gb_Vector3 SKYTOP1=new gb_Vector3(0.1f,0.3f,0.6f);
	public static final gb_Vector3 SKYTOP2=new gb_Vector3(0.25f,0.4f,0.7f);
	public static final gb_Vector3 SKYMID1=new gb_Vector3(1,1,0.3f);
	public static final gb_Vector3 SKYMID2=new gb_Vector3(0.5f,0.8f,1.0f);
	public static final gb_Vector3 SKYBOT1=new gb_Vector3(1.2f,0.5f,0.05f);
	public static final gb_Vector3 SKYBOT2=new gb_Vector3(0.9f,0.9f,1.0f);
	public static final gb_Vector3 FOG1=new gb_Vector3(1,0.5f,0.05f);
	public static final gb_Vector3 FOG2=new gb_Vector3(0.45f,0.65f,0.9f);
	public static final gb_Vector3 AM1=new gb_Vector3(0.5f,0.4f,0.3f);
	public static final gb_Vector3 AM2=new gb_Vector3(0.25f,0.4f,0.7f);
	public static final float g_eps =0.001f;
	public final gb_TerrainMaterials materials;

	public boolean isUseClouds() {
		return useClouds;
	}
	public void setUseClouds(boolean useClouds) {
		this.useClouds = useClouds;
	}
	public boolean isUseExpensiveAO() {
		return useExpensiveAO;
	}
	public void setUseExpensiveAO(boolean useExpensiveAO) {
		this.useExpensiveAO = useExpensiveAO;
	}
	public gb_TerrainSunLighting(gb_TerrainMaterials materials, gb_Vector3 sunDirection, float sunUp , boolean useclouds )
	{ 
		useClouds = useclouds;
		// simple time of day stuff sunset -> sunrise
		this.materials=materials;
		_sc = smoothblend(SKY1, SKY2, sunUp);
		skytop = smoothblend(SKYTOP1, SKYTOP2, sunUp);
		skymid = smoothblend(SKYMID1, SKYMID2, sunUp);      
		skybot = smoothblend(SKYBOT1, SKYBOT2, sunUp);
		fogcol = smoothblend(FOG1,FOG2, sunUp);
		_am  = smoothblend(AM1, AM2, sunUp);
		_sd = sunDirection;     
		_sd.nor();

	}
	gb_Vector3 getShading( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts,
			gb_Vector3 p, gb_Vector3 n,  gb_Vector3 I, gb_TerrainMaterial  mat, float t, int depth, float localAO )
	{
		//? shot shadow ray?
		gb_Vector3 amb;// = new Vector3();
		//amb.set(_am);
		float normFactor = n.y*.5f +.5f;

		float abFactor;
		if (useExpensiveAO)
			abFactor =CalcAO( hgts, p, n,0.01f, 6,4, t);
		else
			abFactor = hgts.GetAO(p.x, p.y, p.z ,t );//

		float shadow=0;
		float nd =Math.max( n.dot(_sd),0);
		if ( nd>0)
			shadow=castShadowRay( qs, hgts, p, _sd, t*qs.shadowRayStartRatio, t );

		// apply bounce depending on in shadow
		float bounceStrength = 1;
		gb_Vector3 bounceCol;//  = new Vector3();
		//bounceCol.set(mat.surfcol);
		//bounceCol.mul(_sc);
		float bshadFactor =1;//(shadow*.5+.5);
		//bounceCol.mul(bshadFactor*bounceStrength);
		bounceCol=mat.surfcol.tmp2().mul(_sc).mul(bshadFactor*bounceStrength);
		amb=_am.tmp().mul(abFactor);//*normFactor);
		amb = m_InterpolateLerp.lerp(normFactor, bounceCol, amb);

		gb_Vector3 sunLight  = new gb_Vector3();
		sunLight.set( _sc);
		sunLight.mul( nd* shadow);
		sunLight.add(amb);

		// specular
		gb_Vector3 hangle;// = new Vector3();
		//hangle.set(I);
		//hangle.mul(-1);
		//hangle.add(_sd);
		//hangle.nor();
		hangle=I.tmp().mul(-1).add(_sd).nor();

		float spec = m_MathUtils.pow16(Math.max(hangle.dot(n),0))*mat.specAmount* shadow;
		gb_Vector3 speccol;// =new Vector3();
		//speccol.set(_sc);
		//speccol.mul(spec);
		speccol=_sc.tmp().mul(spec);

		float edgelight =m_MathUtils.pow4(1- Math.max(-I.dot(n),0)) ;
		edgelight*=mat.rimAmount* shadow*nd;
		gb_Vector3 edgecol;// =new Vector3();
		//edgecol.set(_sc);
		//edgecol.mul(edgelight);
		edgecol=_sc.tmp2().mul(edgelight);


		sunLight.mul(mat.surfcol);  // diffuse
		sunLight.add(speccol);      // spec
		sunLight.add(edgecol);  
		//sunLight.set(shadow,shadow,shadow);
		// sunLight.set(abFactor,abFactor,abFactor);
		//sunLight.set(amb);
		return sunLight;     
	}
	public static float smoothstep (float edge0, float edge1, float x)
	{
		x =  Math.min(Math.max((x - edge0) / (edge1 - edge0),0.0f),1.0f); 
		return x*x*(3-2*x);
	}
	public static gb_Vector3 smoothblend( gb_Vector3 a, gb_Vector3 b, float x)
	{
		x = x*x*(3-2*x);
		return m_InterpolateLerp.lerp(x,a,b);
	}
	// lighting

	// http://developer.nvidia.com/object/siggraph-2008-HBAO.html
	public static float CalcAO( gb_i_TerrainHeightField hgts, gb_Vector3 p, gb_Vector3 nor,
			float sampleRadius, int numSamples,int horizonSteps, float t ) {
		gb_Vector3 n=new gb_Vector3();
		//Vector3 l=new Vector3();

		float tao = 0;
		float off = (float) Math.random();

		// calculate tangent plane

		for (int i = 0; i < numSamples; i++)
		{
			float ang = m_MathUtils.TWO_PI*(((float)i+off)/(float)numSamples);
			n.set( (float)Math.cos(ang), 0.0f, (float)Math.sin(ang));

			float d = sampleRadius;
			float dm = sampleRadius;
			gb_Vector3 tang;// = new Vector3();
			//tang.set(nor);
			//tang.mul(-nor.dot(n));
			//tang.add(n);
			tang=nor.tmp().mul(-nor.dot(n)).add(n);
			assert( Math.abs(tang.dot(nor))<0.01);

			float mgrad =-1000.0f;
			float tanGrad = tang.y/ga_Vector2.ZERO.dst(tang.x,tang.z);
			float tangentAngle = (float) Math.sin(Math.atan(tanGrad));
			// could do multiple samples and cal max gradient
			for (int j =0; j< horizonSteps; j++)
			{
				float hl=hgts.GetHeight(p.x+n.x*d,p.z+n.z*d, t); 
				// get max angle
				float grad = (hl-p.y)/d;
				grad = Math.max( tanGrad, grad);
				mgrad = Math.max(grad, mgrad);
				d += dm;
				dm*=3.;
			}
			float horizonAngle = (float)Math.sin(Math.atan(mgrad));
			tao = tao  + m_MathUtils.clamp(horizonAngle-tangentAngle,0,1);
		}
		float ao =1 - m_MathUtils.clamp(tao/(numSamples),0,1);
		return ao*ao;
	}

	public static	float castShadowRay( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts, gb_Vector3 ro, 
			gb_Vector3 rd, float mint,  float baseT)
	{
		float dt = Math.max( qs.shadowRayStartRatio*mint, 0.001f);
		//float lh = 0.0f;
		//float ly = 0.0f;
		float deltay = 1/Math.max(rd.y,0.1f);
		float maxt = (qs.maxHgt -ro.y )* deltay;  // find maximum distance
		float sratio = qs.shadowRayDeltaRatio;
		deltay = -deltay*0.3f;
		int bsampleCnt = 0;
		gb_Vector3 p = new gb_Vector3();
		float sdist = 0;
		final float sratioM = 1.3f;// random(1.2,1.3);
		for( float t = mint; t < maxt; t += dt )
		{
			p.set(rd);
			p.mul(t);
			p.add(ro);
			float h;
			//numHeightShadowSamples++;
			h = hgts.GetHeight( p.x, p.z ,baseT +t);

			float d = LightAngleRatio *t;
			float sy = p.y - sdist*d*2;
			float sI=(d+h-sy)/((1-sdist)*2*d);
			sdist += Math.max(sI,0);
			if ( sdist>1)
			{
				return 0;
			}

			bsampleCnt++;

			// allow the error to be proportional to the distance
			sratio *=sratioM ;
			dt = sratio*t;
			//lh = h;
			//ly = p.y;
		}
		// println(bsampleCnt);
		return 1 - m_MathUtils.clamp(sdist,0,1);//smoothstep(0,shadowSoftness,sdist);
	}

	public gb_Vector3 applyFog(gb_Vector3 d, gb_Vector3 color, float t )
	{
		float b =1.f/35;
		t = Math.max(t-10,0);
		float fogAmount = (float) Math.exp( -t*b );
		float sunAmount = (float) Math.pow(Math.max( d.dot(_sd), 0.0f ), 6);
		gb_Vector3 fogColor  =  m_InterpolateLerp.lerp(sunAmount*.75f, fogcol, _sc);
		return m_InterpolateLerp.lerp(fogAmount, fogColor, color);
	}
	public gb_Vector3 applyHeightFog(gb_Vector3 p, gb_Vector3 d, gb_Vector3 o, gb_Vector3 c, float t )
	{
		float b =1.f/.5f;
		float e = 0.01f;

		float fa =(float) (e * Math.exp( -(o.y-2)*b ));

		float fogAmount = m_MathUtils.clamp((float) (fa*(1-Math.exp( -t*b*d.y))/d.y),0,1);
		float sunAmount = (float) Math.pow(Math.max( d.dot(_sd), 0.0f ), 6);
		gb_Vector3 fogColor  =  m_InterpolateLerp.lerp(sunAmount*.75f, fogcol, _sc);
		return m_InterpolateLerp.lerp(1-fogAmount, fogColor, c);
	}

	public gb_Vector3 reflection( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts, gb_Vector3 d, gb_Vector3 p, gb_Vector3 n, float oldt, float errFactor, boolean useRefinement)
	{
		float r = 2*d.dot(n);
		gb_Vector3 rv = new gb_Vector3();
		rv.set(n);
		rv.mul(-r);
		rv.add(d);
		rv.nor();

		// always go up
		//rv.y =max(rv.y, 0.001f);
		//rv.normalize();
		gb_Vector3 np = new gb_Vector3();
		np.set(p); 
		gb_Ray ray = new gb_Ray(np,rv);
		gb_Vector3 c;
		float[] pt_t = castRay( qs,hgts, np, rv, 0.05f,30-oldt, Math.max(oldt*2,1), useRefinement);
		if( pt_t[1]>0.0f )
			c= terrainColor( qs, hgts, ray, pt_t[1],1,useRefinement );       
		else
			c= skyColor(ray.direction);
		return applyClouds( c, ray.origin, ray.direction, pt_t[1]>0.0f ? pt_t[1]: 1000.0f, true, true );
	}
	public gb_Vector3 terrainColor( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts, gb_Ray ray, float t, int depth, boolean useRefinement )
	{
		gb_Vector3 p = new gb_Vector3();
		p.set(ray.direction);
		p.mul(t);
		p.add( ray.origin);

		// use central difference height values
		float[] surfhgts= getSurfaceCDS(  hgts, p, t );
		gb_Vector3 sn = genNormal( surfhgts);

		gb_Vector3 m = new gb_Vector3();
		if( p.y < hgts.GetWaterHeight() && depth==0 && qs.useReflection)
		{
			float fogLength = -(hgts.GetWaterHeight()-p.y)/ray.direction.y;
			m = GetWaterSurface( qs, hgts, ray, t, depth, fogLength,p,sn,useRefinement );
			t -= fogLength;
		}
		else
		{
			gb_TerrainMaterial mat = hgts.GetMaterial( p, sn,t );
			//p.y += bumpHeight(p.x, p.z,mat.bs, mat.btile, 3); // add bump mod

			//float[] bumphgts = getBumpCDs(p.x,p.z,mat.bs, mat.btile, t);
			//for(int i =0; i < 4; i++)
			//  surfhgts[i] += bumphgts[i];
			m =getShading( qs, hgts, p, sn /*genNormal(surfhgts)*/, ray.direction,mat, t, depth,0 );
		}
		return applyFog(ray.direction, m, t );
	}

	public gb_Vector3 skyColor(gb_Vector3 d)
	{
		float blendv = (float) Math.sqrt(Math.max(d.y+0.03f,0.000001f));
		float blendv2 = m_MathUtils.clamp(blendv*2-1,0,1);
		blendv = m_MathUtils.clamp(blendv*2,0,1);

		gb_Vector3 sky= m_InterpolateLerp.lerp(blendv, skybot, skymid); 
		sky = m_InterpolateLerp.lerp(blendv2, sky, skytop);

		gb_Vector3 sun = new gb_Vector3();
		sun.set(_sc);
		float sf = (float) Math.pow( Math.max(d.dot(_sd),0), 64);
		sun.mul(sf);
		sky.add(sun);
		return sky;
	}

	public gb_Vector3 genNormal( float[] h)
	{
		gb_Vector3 n = new gb_Vector3 ( h[0]-h[1],  2.0f*g_eps, h[2]-h[3] );
		n.nor();  
		return n;
	}

	public float[] getSurfaceCDS( gb_i_TerrainHeightField hgts, gb_Vector3  p, float t )
	{
		float[] ps = new float[4];  
		ps[0]=hgts.GetFinalHeight(p.x-g_eps,p.z,t);
		ps[1]=hgts.GetFinalHeight(p.x+g_eps,p.z,t);
		ps[2]=hgts.GetFinalHeight(p.x,p.z-g_eps,t);
		ps[3]=hgts.GetFinalHeight(p.x,p.z+g_eps,t);
		return ps;
	}

	public static float[] interp( float t, float dt, float h, float lh, float py, float ly)
	{
		// interpolate the intersection distance
		float[] pt_t = new float[2];
		pt_t[0] = t-dt;
		pt_t[1] = t - dt + dt*(lh-ly)/(py-ly-h+lh);
		return  pt_t;
	}

	public static float[] castRay( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts, gb_Vector3 ro, gb_Vector3 rd, float mint, float maxt, float erratio, boolean useRefinement )
	{
		float dt = Math.max( qs.rayDeltaRatio*mint, 0.001f);
		float lh = 0.0f;
		float ly = 0.0f;
		gb_Vector3 p = new gb_Vector3();
		gb_Vector3 rp = new gb_Vector3();
		float err = erratio;
		erratio = erratio *qs.rayDeltaRatio;

		for( float t = mint; t < maxt; t += dt )
		{
			p.set(rd);
			p.mul(t);
			p.add(ro);
			float h = hgts.GetHeight( p.x, p.z, t*err);
			//g_numHeightSamples++;
			if( p.y < h )
			{
				if ( useRefinement )
				{
					float dt2 = dt*.25f;
					t = t-dt*.75f;
					for (int i=0;i<3;i++,t+=dt2)
					{
						rp.set(rd);
						rp.mul(t);
						rp.add(ro);
						float h0 = hgts.GetHeight( rp.x, rp.z, t );
						if (rp.y<h0)
						{
							return interp( t, dt2, h0, lh, rp.y, ly);    
						}
						lh=h0;
						ly=rp.y;
					}
					dt = dt2;
				}
				return interp( t, dt, h, lh, p.y, ly);         
			}
			// allow the error to be proportional to the distance
			dt = erratio*t;
			lh = h;
			ly = p.y;
		}
		float[] pt_t = new float[]{-1,-1};
		return pt_t;
	}

	gb_Vector3 GetWaterSurface( gb_TerrainQualitySettings qs, gb_i_TerrainHeightField hgts, gb_Ray ray, 
			float t, int depth, float fogLength, gb_Vector3 p, gb_Vector3 n, boolean useRefinement )
	{
		gb_Vector3 m =new gb_Vector3();
		// apply water fog

		float wfd = 1/0.05f;//1/0.25;
		float fm = (float) Math.exp(-fogLength*wfd);
		if ( fm >0.001f)
		{
			gb_TerrainMaterial mat = hgts.GetMaterial( p, n,t );
			m = getShading( qs, hgts, p, n, ray.direction, mat, t, depth,0 );
			m = m_InterpolateLerp.lerp(fm, new gb_Vector3(0,0.12f,0.05f), m);     
		}
		else
			m = new gb_Vector3(0,0.1f,0.05f);

		// find water intersection point
		gb_Vector3 np = new gb_Vector3();
		np.set(ray.direction);
		np.mul(-fogLength);
		np.add(p);

		// get height at this point
		float wd = hgts.GetWaterHeight() -hgts.GetHeight( np.x, np.z,t);
		//				    float foamDepth = 0.02;
		// 
		//				    float nv = max(g_NoiseGen.Fbm( np.x*64.,np.z*64., 3,0.7,2.189),0)*3;
		//				    float foamAmount = constrain(nv-wd/foamDepth,0,1);
		assert( Math.abs(np.y -hgts.GetWaterHeight())<0.0001f);

		float foamAmount = 0;
		int oct = m_NoiseGradient.CalcNoiseOctaves(materials.fovRatio, t, 32f, 2f,1/2.189f);
		float[] d = new float[3];
		materials.g_NoiseGen.dFbm( np.x*32,np.z*32, oct,0.5f,2.189f,d);
		float waterBumpStrength = 12;
		gb_Vector3 waterN = new gb_Vector3( d[1], 32 * waterBumpStrength, d[2]);
		waterN.nor();  
		// add in reflection

		gb_Vector3 ref = reflection( qs, hgts, ray.direction, np, waterN, t, wd, useRefinement);
		m = m_InterpolateLerp.lerp(fresnel( ray.direction, waterN ), m, ref);
		m = m_InterpolateLerp.lerp(foamAmount, m, new gb_Vector3(0.75f,0.75f,0.75f));
		return m;
	}
	
	public gb_Vector3 applyClouds( gb_Vector3 c1, gb_Vector3 p, gb_Vector3 d, float maxT, boolean LQ, boolean cloudShadows)
	{
	  if (!useClouds )
	  {
	    return c1;
	  }
	  int numSteps = LQ ? 4 : 12;//24;//8;//16;//8;
	  
	  float cloudsSize=0.7f;//1.3; //0.7
	  float cloudHeight = 4.5f;//5;//2.5;//- random(0,1/(float)numSteps);
	  float cloudEnd = cloudHeight-cloudsSize;
	  float cloudCover =LQ ? .6f : .7f;
	  final float invCC = 1/cloudCover;
	  float t0 =Math.max((cloudHeight-p.y)/d.y,0);
	  float t1 =(cloudEnd-p.y)/d.y;
	  float tmin =Math.max(Math.min(t0,t1),0);
	  float tmax = Math.max(t0,t1);

	  if ( tmin > maxT || tmax <0.0)
	  {
	    return c1;
	  }
	  
	  gb_Vector3 np = new gb_Vector3();
	  float ccv= 0.0f;

	  //TODO noiseDetail(5,0.7f);
	  gb_Vector3 cCol = new gb_Vector3(1,1,1);

	  float dt = (tmin-tmax)/(float)numSteps;
	  tmin = Math.max(Math.min(maxT,tmin),0);
	  tmax = Math.max(Math.min(maxT,tmax),0);

	  int numShadSamples=0;
	  gb_Vector3 sray = new gb_Vector3();
	  gb_Vector3 snp = new gb_Vector3();
	  snp.set(d);
	  snp.mul(tmin);
	  snp.add(p);
	  sray.set(_sd);
	  sray.mul(10000.0f);
	  sray.sub(snp);
	  sray.nor();
	  
	  sray.mul(new gb_Vector3(0.35f,.5f,0.35f));
	  sray.mul(-dt);
	  
	  gb_Vector3 nd = new gb_Vector3(d.x*0.35f, d.y*.5f,d.z*0.35f);
	  gb_Vector3 nsp = new gb_Vector3(p.x*0.35f+13.5f, p.y*.5f,p.z*0.35f+5.7f);
	  float slabSize = 1/(cloudHeight-cloudEnd);
	  
	  // go back to front
	  // TODO : allow for step size in calc
	  for (float t = tmax+dt*.5f; t > tmin; t+= dt)
	  {
	    np.set(nd);
	    np.mul(t);
	    np.add(nsp);
	    
	    float cv = Math.max(m_Noise.noise3(np.x, np.y,np.z)-cloudCover,0)*invCC;
	    cv = Math.min(cv,1);
	    // blend over the old one
	    cCol.mul(1-cv);
	    gb_Vector3 bcol = new gb_Vector3();
	    float nc ;
	     if (cloudShadows)
	     {
	       float shad=0;
	       gb_Vector3 ss = new gb_Vector3();
	        ss.set(np);
	       
	       for (int i = 0; i < numShadSamples; i++ )
	       {
	         ss.add(sray);
	         shad += Math.max(m_Noise.noise3(ss.x, ss.y,ss.z)-cloudCover,0)*invCC*.5;
	       }
	       nc = m_MathUtils.clamp(1-shad,0,1); 
	       numShadSamples++;
	     }
	     else
	     {
	       float py = p.y + d.y*t;
	       nc = (py-cloudEnd)*slabSize;
	     }
	    nc = nc*0.9f+.1f;
	    nc*=cv;
	    
	    bcol.set(nc,nc,nc);
	    cCol.add(bcol);
	    ccv = (1-cv)*ccv + cv;
	  } 
	  cCol.mul(1.3f);
	  // TODO : add detail noise
	  // from http://freespace.virgin.net/hugo.elias/models/m_clouds.htm
	  float cloudSharpness = 0.75f;
	  //ccv =max(ccv +(noise(np.x*5.,np.z*5.)*2.-1)*0.2,0);
	  float cd= (float) Math.min(Math.pow( ccv, cloudSharpness),1);
	  gb_Vector3 colCol = cCol;//lerp(new Vector3(1.,1.,1.),new Vector3(0.0,0.0,0.0),shadow);//(cd-.75)*4);
	  gb_Vector3 sunTint = new gb_Vector3();
	  sunTint.set(1,1,1);
	  sunTint.add(_sc);
	  sunTint.mul(0.5f);
	  colCol.mul(sunTint);
	  cd *= Math.exp(-tmin*1/50);

	  return m_InterpolateLerp.lerp(cd, c1, colCol);
	}
	
	static final float R=0.05f;
	public static float fresnel( gb_Vector3 i, gb_Vector3 n)
	{
		  float f = Math.abs(i.dot(n));
		  f=(float) Math.pow(1-f,5);
		  return R+(1-R)*(f);
		}
}