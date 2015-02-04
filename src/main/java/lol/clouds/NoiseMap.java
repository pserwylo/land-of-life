package lol.clouds;

public class NoiseMap
{
	private float[][] noiseMap;
	private int width, height;
	
	public NoiseMap(int x, int y)
	{
		this.init(x, y);
	}
	
	public NoiseMap()
	{
		this.init(32, 32);
	}
	
	private void init(int x, int y)
	{
		this.width = x;
		this.height = y;
		this.noiseMap = new float[x][y];
		this.seed();
	}
	
	public void seed()
	{
		float[][] temp = new float[this.width + 2][this.height + 2];
		
		int random = (int)(Math.random() * 5000);
		
		for (int y=1; y<this.height + 1; y++)
		{
			for (int x=1; x<this.width + 1; x++)
			{
				temp[x][y] = 128.0f + this.seedCell(x,  y,  random)*128.0f;
			}
		}
		
		for (int x=1; x<this.width + 1; x++)
		{
			temp[x][0] = temp[x][this.height];
			temp[x][this.height + 1] = temp[x][1];
		}
		
		for (int y=1; y<this.height + 1; y++)
		{
			temp[0][y] = temp[this.width][y];
			temp[this.height + 1][y] = temp[1][y];
		}
		
		temp[0][0] = temp[this.width][this.height];
		temp[this.width + 1][this.height + 1] = temp[1][1];
		temp[0][this.height + 1] = temp[this.width][1];
		temp[this.width + 1][0] = temp[1][this.height];
		
		for (int y=1; y<this.height + 1; y++)
		{
			for (int x=1; x<this.width + 1; x++)
			{
				float center = temp[x][y]/4.0f;
				float sides = (temp[x+1][y] + temp[x-1][y] + temp[x][y+1] + temp[x][y-1])/8.0f;
				float corners = (temp[x+1][y+1] + temp[x+1][y-1] + temp[x-1][y+1] + temp[x-1][y-1])/16.0f;
				
				this.noiseMap[x-1][y-1] = center + sides + corners;
			}
		}
		
		/*for (int x=0; x<this.width; x++)
		{
			for (int y=0; y<this.height; y++)
			{
				this.interpolateCell(x, y);
			}
		}*/
	}
	
	public float interpolateCell(float x, float y)
	{
		int Xint = (int)x;
		int Yint = (int)y;
		
		float Xfrac = x - Xint;
		float Yfrac = y - Yint;
		
		int X0 = Xint % this.width;
		int Y0 = Yint % this.height;
		int X1 = (Xint + 1) % this.width;
		int Y1 = (Yint + 1) % this.height;
		
		float bot = this.noiseMap[X0][Y0] + Xfrac * (this.noiseMap[X1][Y0] - this.noiseMap[X0][Y0]);
		float top = this.noiseMap[X0][Y1] + Xfrac * (this.noiseMap[X1][Y1] - this.noiseMap[X0][Y1]);
		
		return (bot + Yfrac * (top - bot));
	}
	
	private float seedCell(int x, int y, int random)
	{
	    int n = x + y * 57 + random * 131;
	    n = (n<<13) ^ n;
	    return (1.0f - ( (n * (n * n * 15731 + 789221) +
	            1376312589)&0x7fffffff)* 0.000000000931322574615478515625f);
	}
	
	public float getValue(int x, int y)
	{
		return this.noiseMap[x][y];
	}

	public int getWidth(){return this.width;}
	public int getHeight(){return this.height;}
	    
}
