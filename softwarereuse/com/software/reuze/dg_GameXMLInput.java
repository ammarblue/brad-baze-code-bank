package com.software.reuze;

import java.util.ArrayList;
import java.util.Scanner;

import com.software.reuze.dag_GraphFloatingEdges;
import com.software.reuze.dag_GraphFloatingEdgesXMLInput;
import com.software.reuze.dg_Steering;
import com.software.reuze.dg_i_SteeringConstants;
import com.software.reuze.ff_i_XMLInputStates;
import com.software.reuze.vg_i_Renderer;
import com.software.reuze.vgu_PictureArrow;
import com.software.reuze.vgu_PictureCircle;
import com.software.reuze.vgu_PictureImageAnimatedGif;
import com.software.reuze.vgu_PictureImageFile;
import com.software.reuze.vgu_PicturePerson;
import com.software.reuze.vgu_PicturePolygon;




public class dg_GameXMLInput extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
	public int width, height, cursor=-1, partSize, partOverlap, winColor;
	public boolean partOn;
	public String background;
	public dag_GraphFloatingEdges routes;
	public ArrayList<dg_EntityBuilding> buildings;
	public ArrayList<dg_EntityObstacle> stalls;
	public ArrayList<dg_EntityVehicle> vehicles;
	public ArrayList<vg_i_Renderer> pictures=new ArrayList<vg_i_Renderer>();
	private int id, w, h, x, y;
	private String type, image, xml, steering;
	public ArrayList<Integer> colors=new ArrayList<Integer>();
	public dg_World world;
	public dg_EntityDomainRectangular domain;
	private dg_EntityVehicleXMLInput vxml=null;
	public void open(String name) {
		System.out.println("open::"+name);
		if (name.equals("picture")) {colors.clear(); id=-1;}
		if (name.equals("mover")) {
			 vxml=new dg_EntityVehicleXMLInput();
			 vxml.open("vehicles");
			 vxml.open("vehicle");
		}
	}

	public void attribute(String name, String value, String element) {
		
		System.out.println("attr::"+name+" "+value+" "+element);
		if (element.equals("window") && name.equals("x")) width=Integer.parseInt(value);
		if (element.equals("window") && name.equals("y")) height=Integer.parseInt(value);
		if (element.equals("window") && name.equals("image")) background=value;
		if (element.equals("window") && name.equals("color")) {
			getColors(value);
			winColor=colors.get(0);
		}
		//TODO if (element.equals("cursor") && value.equals("cross")) cursor=vg_e_Processing.CROSS;
		if (element.equals("partition") && name.equals("size")) partSize=Integer.parseInt(value);
		if (element.equals("partition") && name.equals("overlap")) partOverlap=Integer.parseInt(value);
		if (element.equals("partition") && name.equals("on")) {
			partOn=value.equals("true");
			world = new dg_World(width, height, partSize, partOverlap);
			world.setNoOverlap(partOn);
			domain = new dg_EntityDomainRectangular(0, 0, width, height);
		}
		if (element.equals("graph") && name.equals("xml")) {
			dag_GraphFloatingEdgesXMLInput gxml=new dag_GraphFloatingEdgesXMLInput();
			gxml.loadXML(app, value, gxml);
			routes = gxml.graph;
		}
		if (element.equals("picture") && name.equals("id")) {id=Integer.parseInt(value); x=-1;}
		if (element.equals("picture") && name.equals("type")) type=value;
		if (element.equals("picture") && name.equals("width")) w=Integer.parseInt(value);
		if (element.equals("picture") && name.equals("height")) h=Integer.parseInt(value);
		if (element.equals("picture") && name.equals("x")) x=Integer.parseInt(value);
		if (element.equals("picture") && name.equals("y")) y=Integer.parseInt(value);
		if (element.equals("picture") && name.equals("image")) image=value;
		if (element.equals("picture") && name.equals("colors")) getColors(value);
		if (element.equals("buildings") && name.equals("xml")) xml=value;
		if (element.equals("buildings") && name.equals("pictureid")) id=Integer.parseInt(value);
		if (element.equals("obstacles") && name.equals("xml")) xml=value;
		if (element.equals("obstacles") && name.equals("pictureid")) id=Integer.parseInt(value);
		if (element.equals("movers") && name.equals("xml")) xml=value;
		if (element.equals("movers") && name.equals("pictureid")) id=Integer.parseInt(value);
		if (element.equals("movers") && name.equals("steering")) steering=value;
		if (vxml!=null) {
			if (name.equals("pictureid")) id=Integer.parseInt(value);
			else if (name.equals("steering")) steering=value;
			else vxml.attribute(name, value, element);
		}
	}
	
	public void getColors(String rgba) {
		Scanner s=new Scanner(rgba); colors.clear();
		while(s.hasNextInt()) {
			int r=s.nextInt(),g=0,b=0,a=255;
			if(s.hasNextInt()) g=s.nextInt();
			if(s.hasNextInt()) b=s.nextInt();
			if(s.hasNextInt()) a=s.nextInt();
			colors.add(app.color(r,g,b,a));
		}
	}

	public void text(String text) {
		// TODO Auto-generated method stub
		
	}
public dg_EntityVehicle patrol;
	public void close(String element) {
		System.out.println("close::"+element);
		if (element.equals("picture")) { //pictures are either added to array or set in the index position
			if (type.equals("Polygon")) {
				vgu_PicturePolygon p=new vgu_PicturePolygon(app,colors.get(0),colors.get(1),(float)w);
				pictures.add(p);
			}
			if (type.equals("Person")) {
				vgu_PicturePerson p=new vgu_PicturePerson(app,colors.get(0),colors.get(1),colors.get(2),(float)w);
				pictures.add(p);  
			}
			if (type.equals("Circle")) {
				vgu_PictureCircle p = new vgu_PictureCircle(app,colors.get(0),colors.get(1),(float)w);
				pictures.add(p);
			}
			if (type.equals("Arrow")) {
				vgu_PictureArrow p = new vgu_PictureArrow(app,colors.get(0),colors.get(1),(float)w);
				pictures.add(p);
			}
			if (type.equals("ImageFile")) {
				vgu_PictureImageFile p = (x<0) ? new vgu_PictureImageFile(app,image)
				                           : new vgu_PictureImageFile(app,image,x,y,w,h);
				pictures.add(p);
			}
		}
		if (element.equals("buildings")) {
			dg_EntityBuildingXMLInput bxml=new dg_EntityBuildingXMLInput();
			bxml.loadXML(app, xml, bxml);
			buildings = bxml.buildingList;
			for (dg_EntityBuilding b : buildings) {
				vg_i_Renderer ir=null;
				if (id>=0) ir=pictures.get(id);
				else {
					if (type.equals("ImageAnimatedGif")) ir = new vgu_PictureImageAnimatedGif(app,b.getColRadius(),image);
				}
			    b.setRenderer(ir);
			    world.addBuilding(b);
			}
		}
		if (element.equals("obstacles")) {
			dg_EntityObstacleXMLInput oxml=new dg_EntityObstacleXMLInput();
			oxml.loadXML(app, xml, oxml);
			stalls = oxml.obstacleList;
			for (dg_EntityObstacle stall : stalls) {
				vg_i_Renderer ir=null;
				if (id>=0) ir=pictures.get(id);
				else {
					if (type.equals("ImageAnimatedGif")) ir = new vgu_PictureImageAnimatedGif(app,stall.getColRadius(),image);
				}
			    stall.setRenderer(ir);
			    world.addObstacle(stall);
			  }
		}
		if (element.equals("movers") || (element.equals("mover"))) {
			if (element.equals("movers")) {
				vxml=new dg_EntityVehicleXMLInput();
				vxml.loadXML(app, xml, vxml);
			} else vxml.close(element);
			dg_Steering sb = new dg_Steering();	// steering behavior used as base for others
			boolean follow=steering !=null && steering.equals("followleader");
			if (steering!=null && steering.equals("wander")) {
				sb.enableBehaviours(dg_i_SteeringConstants.WANDER);
				sb.setWanderDetails(100, 10, 70);
			}
			sb.enableBehaviours(dg_i_SteeringConstants.WALL_AVOID | dg_i_SteeringConstants.OBSTACLE_AVOID);
			sb.setWallAvoidDetails(3, 2.8, 20);
			boolean first=true;
			dg_EntityVehicle fv=null;
			vehicles=vxml.vehicleList;
			for (dg_EntityVehicle tourist : vxml.vehicleList) {
				dg_Steering sbb=(dg_Steering) sb.clone();
				if (follow)
					if (first) {
						sbb.enableBehaviours(dg_i_SteeringConstants.PATH);
						sbb.setWeight(dg_i_SteeringConstants.ARRIVE, 900);
						fv=tourist;
						patrol=fv;
					}
					else {
						sbb.enableBehaviours(dg_i_SteeringConstants.OFFSET_PURSUIT);
						sbb.setWeight(dg_i_SteeringConstants.OFFSET_PURSUIT, 30);
						sbb.setPursuitOffset(ga_Vector2D.sub(tourist.getPos(), fv.getPos()));
						sbb.setAgent(dg_i_SteeringConstants.AGENT_TO_PURSUE, fv);
					}
				tourist.setSB(sbb);
				vg_i_Renderer ir=null;
				if (id>=0) ir=pictures.get(id);
				else {
					if (type.equals("ImageAnimatedGif")) ir = new vgu_PictureImageAnimatedGif(app,tourist.getColRadius(),image);
				}
				tourist.setRenderer(ir);
				tourist.setWorldDomain(domain);
				world.addMover(tourist);
				if (first) first=!first;
			} //for
			vxml=null;
		} //movers
	}
}
