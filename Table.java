import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author bhartisharma
 *
 */
public class Table {

	private Node root;

	/**
	 * Stores the key/value pair in the address book.
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public boolean insert(String key, String value) {
		Node itrTree = root;
		if (root == null) {
			root = new Node(key, value, 0, 0);
		} else {
			while (itrTree != null) {
				if (itrTree.getKey().compareToIgnoreCase(key) > 0) {
					if (itrTree.getLchild() != null) {
						itrTree.setSize(itrTree.getSize() + 1);
						itrTree = itrTree.getLchild();
					} else {
						itrTree.setLchild(new Node(key, value, 0, 0));
						break;
					}

				} else if (itrTree.getKey().compareToIgnoreCase(key) < 0) {

					if (itrTree.getRchild() != null) {
						itrTree.setSize(itrTree.getSize() + 1);
						itrTree = itrTree.getRchild();
					} else {
						itrTree.setRchild(new Node(key, value, 0, 0));
						break;
					}

				} else if (itrTree.getKey().compareToIgnoreCase(key) == 0) {
					break;
				}
			}
			reCalculateHeightAndSize(root);
			return true;
		}
		return false;
	}

	/**
	 * This will calculate the height and size of the Tree using recursion.
	 * @param root
	 */
	private void reCalculateHeightAndSize(Node root) {
		if (root == null) {
			return;
		}
		root.setHeight(calculateHeight(root));
		root.setSize(calculateSize(root));
		reCalculateHeightAndSize(root.getLchild());
		reCalculateHeightAndSize(root.getRchild());
	}

	/**
	 * This will return the size of the node recursively.
	 * @param node
	 * @return integer
	 */
	private int calculateSize(Node root) {
		if (root == null) {
			return 0;
		} else {
			return calculateSize(root.getLchild()) + 1 + calculateSize(root.getRchild());
		}
	}

	/**
	 * This will calculate the height of the given node recursively.
	 * @param root
	 * @return integer
	 */
	private int calculateHeight(Node root) {
		int leftHeight, rightHeight;
		if (root == null)
			return -1;
		leftHeight = calculateHeight(root.getLchild());
		rightHeight = calculateHeight(root.getRchild());
		if (leftHeight > rightHeight) {
			return 1 + leftHeight;
		} else {
			return 1 + rightHeight;
		}
	}

	/**
	 * Looks up the entry with the given key and returns the associated value. If no entry is found null is returned.
	 * @param key
	 * @return String
	 */
	public String lookUp(String key) {
		return lookUpRecurvive(key, root);
	}

	/**
	 * This will recursively look for the value, if not found it will return the empty string.
	 * @param key
	 * @param root
	 * @return String
	 */
	private String lookUpRecurvive(String key, Node root) {
		if (root == null) {
			return "";
		} else if (0 == root.getKey().compareToIgnoreCase(key)) {
			return root.getValue();
		} else if (0 < root.getKey().compareToIgnoreCase(key)) {
			return lookUpRecurvive(key, root.getLchild());
		} else {
			return lookUpRecurvive(key, root.getRchild());
		}
	}

	/**
	 * Deletes the entry with the given key.
	 * @param key
	 * @return boolean
	 */
	public boolean deleteContact(String key) {
		if ( "" != lookUp( key ) ) {
			this.root = deleteContactRecursively(key, root);
			reCalculateHeightAndSize(this.root);
			return true;
		}
		
		return false;
	}

	/**
	 * This will delete the contact using recursion.
	 * @param key
	 * @param root
	 * @return Node
	 */
	private Node deleteContactRecursively(String key, Node root) {
		Node temp = root;
		if (temp == null) {
			return null;
		} else if (0 < temp.getKey().compareToIgnoreCase(key)) {
			temp.setLchild(deleteContactRecursively(key, temp.getLchild()));
		} else if (0 > temp.getKey().compareToIgnoreCase(key)) {
			temp.setRchild(deleteContactRecursively(key, temp.getRchild()));
		} else {

			if (temp.getLchild() == null) {

				return root.getRchild();

			} else if (root.getRchild() == null) {

				return root.getLchild();
			}

			Node tempNode = temp.getRchild();

			while (tempNode.getLchild() != null) {
				tempNode = tempNode.getLchild();
			}

			temp.setKey(tempNode.getKey());
			temp.setRchild(deleteContactRecursively(temp.getKey(), temp.getRchild()));
		}
		return temp;
	}

	/**
	 * 
	 * Replaces the old value associated with with the given key with the newValue string.
	 * @param key
	 * @param newValue
	 * @return boolean
	 */
	public boolean update(String key, String newValue) {
		Node itrTree = root;
		while (itrTree != null) {
			if (itrTree.getKey().compareToIgnoreCase(key) > 0) {
				itrTree = itrTree.getLchild();
			} else if (itrTree.getKey().compareToIgnoreCase(key) < 0) {
				itrTree = itrTree.getRchild();
			} else if (itrTree.getKey().compareToIgnoreCase(key) == 0) {
				itrTree.setValue(newValue);
				return true;
			}
		}
		return false;
	}

	/**
	 * Displays Name/Address for each table entry, the list of entries is sorted by the keys.
	 * @return integer
	 */
	public int displayAll() {
		displayAllRecursively(root);
		System.out.println("Tree size = " + root.getSize());
		System.out.println("Number of contacts in addressbook = " + root.getSize());
		return root.getSize();
	}

	/**
	 * This is a recursive in-order traversal.
	 * @param root
	 */
	private void displayAllRecursively(Node root) {
		if (root == null)
			return;
		displayAllRecursively(root.getLchild());
		System.out.println(root.getKey());
		System.out.println(root.getValue());
		System.out.println("     --- Node height = " + root.getHeight());
		System.out.println();
		displayAllRecursively(root.getRchild());
	}

	/**
	 *  reads the name of a text output file, and will write a list of the table entries to an the output file.
	 * @throws IOException
	 */
	public void save() throws IOException {
		System.out.print("Please enter the file name: ");
		@SuppressWarnings("resource")
		Scanner scn = new Scanner(System.in);
		String key = scn.nextLine();
		FileWriter fWriter = new FileWriter(key + ".txt");
		BufferedWriter writer = new BufferedWriter(fWriter);
		saveRecursively(writer, root);
		writer.newLine();
		writer.close();
	}

	/**
	 * This will save the file recursively.
	 * @param writer
	 * @param root
	 * @throws IOException
	 */
	private void saveRecursively(BufferedWriter writer, Node root) throws IOException {
		if (root == null) {
			return;
		}
		writer.write(root.getKey() + "\n");
		writer.write(root.getValue() + "\n");
		saveRecursively(writer, root.getLchild());
		saveRecursively(writer, root.getRchild());
	}
}
