package lol.clouds;

public class Clouds
{
	private float[][] cloudMap;
	private float[][] cloudCellMap;
	private int width, height, cloudCellWidth, cloudCellHeight;
	private NoiseMap noise;
	private float maxValue;
	
	/**
	 * Higher heat values generate less cloud cover. values should range between about 0 and 200
	 */
	public static int HEAT = 50;
	
	public Clouds(int width, int height)
	{
		this.init(width, height);
	}
	
	public Clouds()
	{
		this.init(25, 25);
	}
	
	private void init(int width, int height)
	{
		this.width = width*10;
		this.height = height*10;
		this.cloudCellWidth = width;
		this.cloudCellHeight = height;
		this.cloudCellMap = new float[this.cloudCellWidth][this.cloudCellHeight];
		this.seed();
		this.transfer();
		this.normalize();
	}
	
	public float getCloudCellAt(int x, int y)
	{
		while (x < 0) x += this.cloudCellWidth;
		while (y < 0) y += this.height;
		if (x >= this.cloudCellWidth) x %= this.cloudCellWidth-1;
		if (y >= this.cloudCellHeight) y %= this.cloudCellHeight-1;
		return this.cloudCellMap[x][y];
	}
	
	/**
	 * Normalize all values between 0 and 1...
	 */
	private void normalize()
	{
		for (int x = 0; x < this.width/10; x ++)
		{
			for (int y = 0; y < this.height/10; y ++)
			{
				this.cloudCellMap[x][y] = Math.max(this.cloudCellMap[x][y] - HEAT, 0) / this.maxValue;
			}	
		}
	}
	
	public void seed()
	{
		this.cloudMap = new float[this.width][this.height];
		this.noise = new NoiseMap(this.width/2, this.height/2);
		this.overlapOctaves();
		this.expFilter();
	}
	
	/**
	 * Transfers a highly detailed cloud map generated from a perlin noise
	 * function into a less detailed map. This is done by averaging out the
	 * cells values...
	 */
	private void transfer()
	{
		for (int y = 0; y < this.height/10; y++)
		{
			for (int x = 0; x < this.width/10; x++)
			{
				float value = 0.0f;
				int count = 0;
				
				for (int i = y*10 - 5; i < y*10 + 5; i++)
				{
					for (int j = x*10 - 5; j < x*10 + 5; j++)
					{
						if (i < 0) i += this.height;
						if (i > this.height) i %= this.height;
						if (j < 0) j += this.width;
						if (j > this.width) j %= this.width;
						
						value += this.cloudMap[j][i];
						count ++;
					}					
				}
				
				value /= count;
				this.cloudCellMap[x][y] = value;
			}
		}
	}
	
	private void expFilter()
	{
		float cover = 20.0f;
		float sharpness = 0.95f;
		
		for (int y=0; y<this.height; y++)
		{
			for (int x=0; x<this.width; x++)
			{
				float c = this.cloudMap[x][y] - (255.0f-cover);
				if (c<0)     c = 0;
				this.cloudMap[x][y] = 255.0f - ((float)(Math.pow(sharpness, c))*255.0f);
			}
		}
	}
	
	private void overlapOctaves()
	{	
		for (int octave=0; octave<12; octave++)
		{
			for (int x=0; x<this.width; x++)
			{
				for (int y=0; y<this.height; y++)
				{
					float scale = (float)(1 / Math.pow(2, 3-octave));
					float noise = this.noise.interpolateCell(x*scale, y*scale);
					cloudMap[x][y] += noise / Math.pow(2, octave);
				}
			}
		}
		this.maxValue = this.obtainMax();
	}
	
	public float obtainMax()
	{
		float max = 0;
		for (int x=0; x<this.width; x++)
		{
			for (int y=0; y<this.height; y++)
			{
				if (this.cloudMap[x][y] > max)
				{
					max = this.cloudMap[x][y];
				}
			}
		}
		return max;
	}
	
	public float getValue(int x, int y)
	{
		return this.cloudCellMap[x][y];
	}

	public float getMaxValue(){return this.maxValue;}
	public int getWidth(){return this.width/10;}
	public int getHeight(){return this.height/10;}

}
