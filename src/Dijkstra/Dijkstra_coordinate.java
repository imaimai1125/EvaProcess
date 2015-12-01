package Dijkstra;

enum CellKind{
	NORM,
	EXIT,
	WALL,
	OBST,
}

class Node{
	private int y,x;		//ラベル（任意の文字列）
	private int totalCost; 	//コスト(ある道を通ったときのコスト)
	private Node from; 		//そのノードに達する前のノード
	private CellKind cell;

	//constructor

	//スタートではない
	public Node(int y, int x, CellKind cell, Node from){
		this.y = y;
		this.x = x;
		this.cell = cell;
		this.from = from;
	}
	//スタート地点
	public Node(int y, int x, CellKind cell){
		this(y,x, cell,null);
	}

	//ゲッタ
	public int getY(){return y;}
	public int getX(){return x;}
	public CellKind getCell(){return cell;}
	public Node getFrom(){return from;}
	public int gettotalCost(){return totalCost;}
	//セッタ
	public void setFrom(Node from){this.from = from;}
	public void settotalCost(int totalCost) {this.totalCost = totalCost;}
}//class Node


class Route{
	private Node srcNode, dstNode;		//スタートと目的地のノード
	private int cost;					//ノード間にかかるコスト
	//コンストラクタ
	//はじめにsourceとdistination,costを全部設定しておく（静的変化。動的にするにはどうする??)
	public Route(Node srcNode, Node dstNode, int cost){
		this.srcNode = srcNode;
		this.dstNode = dstNode;
		this.cost = cost;
	}

	//ゲッタ
	public Node getSrcNode(){return srcNode;}
	public Node getDstNode(){return dstNode;}
	public int getCost(){return cost;}

}//class Route


public class Dijkstra_coordinate{
	public static void main(String[] args){
		int cell_x = 5;
		int cell_y = 5;
		// initialization
		//通常ノードとwallの設定
		Node[][] nodes;
		nodes = new Node[cell_y+2][cell_x+2];
		for (int i=0;i<=cell_y+1;i++){
			for(int j=0;j<=cell_x+1;j++){
				if(i==0||j==0||i==cell_y+1||j==cell_x+1){
					nodes[i][j] = new Node(i,j,CellKind.WALL);
				}else{
					nodes[i][j] = new Node(i,j,CellKind.NORM);
				}
			}
		}
		//出口と障害物
		nodes[cell_y][cell_x] = new Node(cell_y,cell_x,CellKind.EXIT);
		nodes[2][3] = new Node(2,3,CellKind.OBST);
		nodes[2][4] = new Node(2,3,CellKind.OBST);

		//ルート選定
		Route[][][] routes;
		routes = new Route[cell_x+1][cell_y+1][4];
		for (int i=0;i<cell_y;i++){
			for(int j=0;j<cell_x;j++){

				//上
				if(nodes[i][j+1].getCell() == CellKind.NORM||nodes[i][j+1].getCell() == CellKind.EXIT){
					routes[i+1][j+1][0] = new Route(nodes[i+1][j+1],nodes[i][j+1],1);
				}else if(nodes[i][j+1].getCell() == CellKind.WALL||nodes[i][j+1].getCell() == CellKind.OBST){
					routes[i+1][j+1][0] = new Route(nodes[i+1][j+1],nodes[i][j+1],1000);
				}
				//下
				if(nodes[i+2][j+1].getCell() == CellKind.NORM||nodes[i+2][j+1].getCell() == CellKind.EXIT){
					routes[i+1][j+1][1] = new Route(nodes[i+1][j+1],nodes[i+2][j+1],1);
				}else if(nodes[i+2][j+1].getCell() == CellKind.WALL||nodes[i+2][j+1].getCell() == CellKind.OBST){
					routes[i+1][j+1][1] = new Route(nodes[i+1][j+1],nodes[i+2][j+1],1000);
				}
				//右
				if(nodes[i+1][j+2].getCell() == CellKind.NORM||nodes[i+1][j+2].getCell() == CellKind.EXIT){
					routes[i+1][j+1][2] = new Route(nodes[i+1][j+1],nodes[i+1][j+2],1);
				}else if(nodes[i+1][j+2].getCell() == CellKind.WALL||nodes[i+1][j+2].getCell() == CellKind.OBST){
					routes[i+1][j+1][2] = new Route(nodes[i+1][j+1],nodes[i+1][j+2],1000);
				}
				//左
				if(nodes[i+1][j].getCell() == CellKind.NORM||nodes[i+1][j].getCell() == CellKind.EXIT){
					routes[i+1][j+1][3] = new Route(nodes[i+1][j+1],nodes[i+1][j],1);
				}else if(nodes[i+1][j].getCell() == CellKind.WALL||nodes[i+1][j].getCell() == CellKind.OBST){
					routes[i+1][j+1][3] = new Route(nodes[i+1][j+1],nodes[i+1][j],1000);
				}
			}
		}
		//情報表示
		for (int i=0;i<=cell_y+1;i++){
			for(int j=0;j<=cell_x+1;j++){
				System.out.print(nodes[i][j].getCell()+" ");
			}
			System.out.println();
		}
		for (int i=1;i<cell_y+1;i++){
			for(int j=1;j<cell_x+1;j++){
				System.out.print("("+routes[i][j][0].getCost()+" "
									+routes[i][j][1].getCost()+" "
									+routes[i][j][2].getCost()+" "
									+routes[i][j][3].getCost()+") ");
			}
			System.out.println();
		}
//
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
//			System.out.println(node.getX()+","+node.getY() );
//			node = node.getFrom();
//		}
	}
}

