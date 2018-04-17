package tp.maze.items;

public class Inventory {

	public Inventory(int size) throws Exception {
		if(size % 5 != 0) throw new Exception("ERROR: Inventory size must be multiple of 5 - Size received: [" + size + "]");
		this.size = size;
		contents = new Item[size];
		for(int i = 0; i < size; i++) {
			contents[i] = Items.AIR;
		}
	}

	private int size = 9;
	private Item[] contents;
	
	public boolean addItem(Item item) {
		for(int i = 0; i < size; i++) {
			if(contents[i].getType() == ItemType.AIR) {
				contents[i] = item;
				return true;
			}
		}
		return false;
	}
	public void setItem(int position, Item item) {
		contents[position] = item;
	}
	public Item getItem(int position) throws Exception {
		if(contents[position] != null) {
			return contents[position];
		}else {
			throw new Exception("ERROR: Position [" + position + "] is out of bounds - Max position [" + (size - 1) + "]");
		}
	}
	
	public boolean hasItem(String ID) {
		for(Item i : contents) {
			try{if(i.getID().equals(ID)) return true;}catch(Exception ex) {}
		}
		return false;
	}
	
	private int selectedItem = -1;
	public int getSelectedItem() {
		return selectedItem;
	}
	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	public int getSize() {
		return size;
	}
	public Item[] getContents() {
		return contents;
	}
	public void setContents(Item[] contents) throws Exception {
		if(contents.length > size) {
			throw new Exception("ERROR: Inventory size too big - Expected: [" + size + "] Received: [" + contents.length + "]");
		}else {
			this.contents = contents;
		}
	}
	
}
