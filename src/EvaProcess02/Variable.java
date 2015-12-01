package EvaProcess02;
/**
 *
 * @author imaimai
 * 処理時の変数を記述したもの
 * 初期設定しなければならない変数とかは全部ここに記述
 * static : 初期設定をしたらもう変えない。その他は変えてよい
 */
public class Variable {
	static int L;
	static int N;
	int time_interval;
	int updaterule;
	int n;
	//出口の設定（位置と大きさ）
	static int exit1_x,exit1_y, exit1_size;
	static int exit2_x,exit2_y, exit2_size;
	//Particleオブジェクトとしてcellを定義
	Cell cell[][];
	Cell celltemp_lower[][];
	Cell celltemp_upper[][];
	Cell celltemp_right[][];
	Cell celltemp_left[][];
	Cell celltemp_stay[][];
	double DFF_alpha;//DFFの拡散係数
	double DFF_delta;//DFFの蒸発係数
	//粒子の定義
	double ks_haste;	//SFFの係数（haste粒子)
	double ks_normal;//SFFの係数（normal粒子)
	//コンストラクタ(Variableが呼び出されたときの初期設定）
	int particle_num;//粒子の個数

	public Variable(){
		/**************************************
		 * 書き換え不可能（初期値のまま）
		 *************************************/
		//Nが縦(y)方向, Lが横(x)方向のセルの個数
		this.N = 30;
		this.L = 30;
		//セル生成
		this.cell = new Cell[this.N+2][this.L+2];
		//一時的な格納場所。←→↑↓
		this.celltemp_lower = new Cell[this.N+2][this.L+2];
		this.celltemp_upper = new Cell[this.N+2][this.L+2];
		this.celltemp_left = new Cell[this.N+2][this.L+2];
		this.celltemp_right = new Cell[this.N+2][this.L+2];
		this.celltemp_stay = new Cell[this.N+2][this.L+2];
		//出口の設定
		this.exit1_size = 1;
		this.exit2_size = 1;
		this.exit1_x  = 1;
		this.exit2_x  = this.L-this.exit2_size;
		this.exit1_y  = this.N;
		this.exit2_y = this.N;
		/**************************************
		 * 書き換え可能な変数群
		 **************************************/
		//アップデートルール（複数処理を扱うときに。今後の拡張として）
		this.updaterule = 1;
		//倍率（描画アプレットの大きさ調整）
		this.n = 10;
		//アニメーションの更新間隔
		this.time_interval = 300;
		//DFFの設定
		this.DFF_alpha = 0.5;
		this.DFF_delta  = 0.5;
		this.particle_num = 0;
		}
	//座標の抽出
	/***************************************
	 * 効率化絶対できる。わからないので聞く10/10
	 ****************************************/
	public int[] get_coordinate(int number){
		int[] coordinate = new int[2];
		for(int i=1; i<this.N+1;i++){
			for(int j=1;j<this.L+1;j++){
				if(this.cell[i][j].get_number() == number){
					coordinate[0] = i;
					coordinate[1] = j;
				}
			}
		}
		return coordinate;
	}
}