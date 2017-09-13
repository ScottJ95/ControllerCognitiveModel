package adt;


/**
 * Object Type
 * Contains the type of object that was identified. This will be used to determine how 
 * to parse the information on each specific radar element.
 * Status: Completed 
 * Last update: 04/13/2015
 * @author: Rithvik Mandalapu
 * @version: 04/13/2015
*/

public  class ObjectType 
{
	

	public enum ObjType
	{
		LDB,
		FDB,
		PLANE,
		OTHER
	}

        ObjType type;
	
	
	public ObjectType(ObjType type)
	{
		this.type = type;
		
	}
	
	public ObjectType()
	{
		this.type = ObjType.OTHER;
	}
	
	
	public ObjType getObjectType()
	{
		return this.type;
	}
	
	public void setObjectType(ObjType type)
	{
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return "ObjectType []";
	}

}
