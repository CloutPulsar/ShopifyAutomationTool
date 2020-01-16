
//Credit to Oneiros for the OOP implementation
public class Element implements Comparable<Element>
{
	int index, value;

	public Element(int index, int value)
	{
		this.index = index;
		this.value = value;
	}

	public int compareTo(Element e)
	{
		return this.value - e.value;
	}
}
