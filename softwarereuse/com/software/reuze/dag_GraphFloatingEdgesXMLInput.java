package com.software.reuze;


public class dag_GraphFloatingEdgesXMLInput extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
	public dag_GraphFloatingEdges graph;
	dag_GraphNode currentN;
	int from, to;
	double costout, costback;
	public void open(String name) {
		System.out.println("open::"+name);
		if (name.equals("graph")) graph = new dag_GraphFloatingEdges();
		if (name.equals("node")) currentN = new dag_GraphNode();
		if (name.equals("edge")) costout=costback=1;
	}

	public void attribute(String name, String value, String element) {
		System.out.println("attr::"+name+" "+value+" "+element);
		if (element.equals("node") && name.equals("id")) currentN.id=Integer.parseInt(value);
		if (element.equals("node") && name.equals("x")) currentN.x=Double.parseDouble(value);
		if (element.equals("node") && name.equals("y")) currentN.y=Double.parseDouble(value);
		if (element.equals("edge") && name.equals("from")) from=Integer.parseInt(value);
		if (element.equals("edge") && name.equals("to")) to=Integer.parseInt(value);
		if (element.equals("edge") && name.equals("cost_out")) costout=Double.parseDouble(value);
		if (element.equals("edge") && name.equals("cost_back")) costback=Double.parseDouble(value);
	}

	public void text(String text) {
		// TODO Auto-generated method stub
		
	}

	public void close(String element) {
		System.out.println("close::"+element);
		if (element.equals("node")) graph.addNode(currentN);
		if (element.equals("edge")) {
			/*if (costout==costback) graph.addEdge(from, to, costout); TODO AStar assumes 2 edges even if cost the same
			else*/ graph.addEdge(from, to, costout, costback);
		}
	}

}
