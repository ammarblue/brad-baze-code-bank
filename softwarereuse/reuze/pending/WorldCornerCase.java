package reuze.pending;

/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


//import straightedge.geom.*;
import java.util.*;

import com.software.reuze.ga_Polygon;
import com.software.reuze.ga_Vector2;



/**
 *
 * @author Phillip
 */
public class WorldCornerCase extends GameWorld{
	public WorldCornerCase(){

	}
	protected ArrayList<ga_Polygon> makePolygons(){
		ArrayList<ga_Polygon> allPolys = new ArrayList<ga_Polygon>();
		int width = 20;
		ga_Polygon poly = ga_Polygon.createRectOblique(new ga_Vector2(50, 200), new ga_Vector2(200, 200), width);
		ga_Polygon poly2 = ga_Polygon.createRectOblique(new ga_Vector2(200 - width/2, 200 + width/2), new ga_Vector2(200 - width/2, 50 + width/2), width);

		ga_Polygon poly3 = ga_Polygon.createRectOblique(new ga_Vector2(300, 200), new ga_Vector2(400, 200), width);
		ga_Polygon poly4 = ga_Polygon.createRectOblique(new ga_Vector2(350, 200), new ga_Vector2(450, 200), width);
		ga_Polygon poly5 = ga_Polygon.createRectOblique(new ga_Vector2(375, 100), new ga_Vector2(375, 300), width);
		
		allPolys.add(poly);
		allPolys.add(poly2);
		allPolys.add(poly3);
		allPolys.add(poly4);
		allPolys.add(poly5);

		ga_Polygon circle = ga_Polygon.createRegularPolygon(1020, 50);
		circle.translateTo(200, 400);
		allPolys.add(circle);

		return allPolys;
	}
}
