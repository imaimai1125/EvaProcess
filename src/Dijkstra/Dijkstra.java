//package Dijkstra;
//
//public class Dijkstra{
//	public static void main(String[] args){
//		// initialization
//		Node dummy = new Node ('X');
//		Node s = new Node('S', dummy);
//		Node a = new Node('A');
//		Node b = new Node('B');
//		Node c = new Node('C');
//		Node d = new Node('D');
//		Node g = new Node('G');
//
//		Route[] routes = {
//			new Route(s,a,5),
//			new Route(s,b,4),
//			new Route(s,c,2),
//			new Route(a,b,2),
//			new Route(a,g,6),
//			new Route(b,c,3),
//			new Route(c,d,6),
//			new Route(b,d,2),
//			new Route(d,g,4),
//		};
//
//		while(true){
//			boolean cont = false;
//			// calucuation
//			//拡張for文
//			//routesの成分をインデックスをインクリメントしながらアレする
//			for(Route route : routes){
//				Node src = route.getSrcNode();
//				Node dst = route.getDstNode();
//				if(src.getFrom() == null) continue;
//				if(dst.getFrom() == null ||
//				   src.gettotalCost()+route.getCost()<dst.gettotalCost()){
//					dst.setFrom(src);
//					dst.settotalCost(src.gettotalCost() + route.getCost());
//					cont = true;//フラグ
//				}
//			}
//			if (!cont) break;
//		}
//		// output
//		System.out.println("distance : " + g.gettotalCost());
//		Node node = g;
//		while(node != dummy){
//			System.out.println(node.getLabel());
//			node = node.getFrom();
//		}
//	}
//}
//
//class Node{
//	private char label;		//ラベル（任意の文字列）
//	private int totalCost; 	//コスト(ある道を通ったときのコスト)
//	private Node from; 		//そのノードに達する前のノード
//	//constructor
//
//	//スタートではない
//	public Node(char label, Node from){
//		this.label = label;
//		this.from = from;
//	}
//	//スタート地点
//	public Node(char label){
//		this(label, null);
//	}
//
//	//ゲッタ
//	public char getLabel(){return label;}
//	public Node getFrom(){return from;}
//	public int gettotalCost(){return totalCost;}
//	//セッタ
//	public void setFrom(Node from){this.from = from;}
//	public void settotalCost(int totalCost) {this.totalCost = totalCost;}
//}//class Node
//
//
//class Route{
//	private Node srcNode, dstNode;		//スタートと目的地のノード
//	private int cost;					//ノード間にかかるコスト
//	//コンストラクタ
//	//はじめにsourceとdistination,costを全部設定しておく（静的変化。動的にするにはどうする??)
//	public Route(Node srcNode, Node dstNode, int cost){
//		this.srcNode = srcNode;
//		this.dstNode = dstNode;
//		this.cost = cost;
//	}
//
//	//ゲッタ
//	public Node getSrcNode(){return srcNode;}
//	public Node getDstNode(){return dstNode;}
//	public int getCost(){return cost;}
//
//}//class Route
//
