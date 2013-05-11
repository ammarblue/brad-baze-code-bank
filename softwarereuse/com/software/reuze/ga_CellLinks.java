package com.software.reuze;

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
/**
 *
 * @author Keith
 */
public class ga_CellLinks {
	ga_Vector2 point;
	ga_CellRectangle upLink;
	ga_CellRectangle downLink;
	ga_CellRectangle leftLink;
	ga_CellRectangle rightLink;

	public ga_CellLinks(ga_Vector2 point){
		this.point = point;
	}

	public ga_Vector2 getPoint() {
		return point;
	}

	public void setPoint(ga_Vector2 point) {
		this.point = point;
	}

	public ga_CellRectangle getDownLink() {
		return downLink;
	}

	public void setDownLink(ga_CellRectangle downLink) {
		this.downLink = downLink;
	}

	public ga_CellRectangle getLeftLink() {
		return leftLink;
	}

	public void setLeftLink(ga_CellRectangle leftLink) {
		this.leftLink = leftLink;
	}

	public ga_CellRectangle getRightLink() {
		return rightLink;
	}

	public void setRightLink(ga_CellRectangle rightLink) {
		this.rightLink = rightLink;
	}

	public ga_CellRectangle getUpLink() {
		return upLink;
	}

	public void setUpLink(ga_CellRectangle upLink) {
		this.upLink = upLink;
	}


}
