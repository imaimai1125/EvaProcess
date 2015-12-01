package EvaProcess02;

public class Cell {
	//セル内粒子の種類
	public enum ParticleKind{
		NORMAL,
		HASTE,
		EMPTY,
	}
	//そもそものセルの種類
	static enum CellKind{
		NORMAL,
		EXIT,
		ENTRANCE,
		OBSTACLE,
		WALL,
	}

	public boolean Group;
	//クラス内変数の定義
	public CellKind cell;
	public ParticleKind particle;
	public double SFF;
	public double DFF;
	public double haste_index;   //急ぎ度（どのくらい相手に譲るか）
	public int number;
	/*************************************
	 * コンストラクタ
	 *************************************/
	Cell(int number,Variable var,CellKind cell,ParticleKind particle){
		this.cell = cell;
		this.particle = particle;
		this.SFF = 0;
		this.DFF = 0;
		this.number = number;
	}
	/*************************************
	 * ゲッタ
	 *************************************/
	public CellKind get_cellkind(){
		return this.cell;
	}
	//スピードを取り出す。(厳密には速度ではないが、その類のもの）
	public double get_ks(){
		if(this.particle == particle.HASTE){
			return 1.0;
		}else if(this.particle == particle.NORMAL){
			return 1.0;
		}else{
			return 0;
		}
	}
	//DFFの係数
	public double get_kd(){
		if(this.particle == particle.HASTE){
			return 0.0;
		}else if(this.particle == particle.NORMAL){
			return 0.0;
		}else{
			return 0;
		}
	}
	//急ぎ度を取り出す（譲らない確率）
	public double get_haste_index(){
		if(this.particle == particle.HASTE){
			return 1.0;
		}else if(this.particle == particle.NORMAL){
			return 0.0;
		}else{
			return 0.0;
		}
	}
	//粒子の番号を取り出す
	public int get_number(){
		return this.number;
	}
	public ParticleKind get_particlekind(){
		return this.particle;
	}
	/*************************************
	 * セッタ
	 *************************************/
	//粒子の種類を変更する
	public ParticleKind set_kind(ParticleKind particle){
		return this.particle;
	}
	public void set_number(int number){
		this.number = number;
	}
}

