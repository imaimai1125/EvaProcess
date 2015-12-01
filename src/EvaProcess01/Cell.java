package EvaProcess01;

public class Cell {
	//セル内粒子の種類
	public enum ParticleKind{
		NORMAL,
		HASTE,
		EMPTY,
	}
	//そもそものセルの種類
	public enum CellKind{
		NORMAL,
		EXIT,
		ENTRANCE,
		OBSTACLE,
		WALL,
	}

	//クラス内変数の定義
	//11/25 ここはprivateにしたい。
	public CellKind cell;
	public ParticleKind particle;
	public double SFF;
	public double DFF;
	public double haste_index;   //急ぎ度（どのくらい相手に譲るか）
	//コンストラクタ
	Cell(Variable var,CellKind cell,ParticleKind particle){
		this.cell = cell;
		this.particle = particle;
		this.SFF = 0;
		this.DFF = 0;
	}
	//ゲッタ
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

	//ゲッタ(particleの種類をgetする)
	public ParticleKind get_particlekind(){
		return this.particle;
	}
	//セッタ(particleの種類を変える)
	public ParticleKind set_particlekind(ParticleKind particle){
		return this.particle;
	}
	
}

