import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head, tail;
	private int size;
	private int modCount;

	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(head);
		head = newNode;
		if(tail == null) {
			tail = head;
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(tail != null) {
			tail.setNext(newNode);
		}
		else {
			head = newNode;
		}
		tail = newNode;
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<T> current = head;
		boolean found = false;
		while(!found && current != null) {
			if(current.getElement().equals(target)) {
				found = true;
			}
			else {
				current = current.getNext();
			}
		}
		if(!found) {
			throw new NoSuchElementException();
		}
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(current.getNext());
		current.setNext(newNode);
		if(current == tail) {
			tail = newNode;
		}
		
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		if(index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> newNode = new LinearNode<T>(element);
		LinearNode<T> current = head;
		for(int i = 0; i < index-1; i++) {
			current = current.getNext();
		}
		if(index == 0 && current != null) {
			newNode.setNext(current);
			head = newNode;
		}
		else if(current != null) {
			newNode.setNext(current.getNext());
			current.setNext(newNode);
			if(tail == current) {
				tail = newNode;
			}
		}
		else {
			head = tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if(isEmpty()) { //head == null or tail == null or size == 0 or size()==0
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		head = head.getNext();
		if(head == null) {
			tail = null;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		if(size != 1) {
			LinearNode<T> newTail = head;
			while(newTail.getNext() != tail) {
				newTail = newTail.getNext();
			}
			newTail.setNext(null);
			tail = newTail;
		}
		else {
			head = tail = null;
		}

		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		boolean found = false;
		LinearNode<T> previous = null;
		LinearNode<T> current = head;

		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}

		if (!found) {
			throw new NoSuchElementException();
		}

		if (size() == 1) { //only node
			head = tail = null;
		}
		else if (current == head) { //first node
			head = current.getNext();
		}
		else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		}
		else { //somewhere in the middle
			previous.setNext(current.getNext());
		}

		size--;
		modCount++;

		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
		T retVal = null;
		LinearNode<T> current = head;
		for(int i = 0; i < index-1; i++) {
			current = current.getNext();
		}
		if(index == 0) { //target is head
			retVal = head.getElement();
			head = head.getNext();
			if(head == null) {
				tail = null;
			}
		}
		else if(current.getNext() == tail) { //target is tail
			retVal = tail.getElement();
			current.setNext(null);
			tail = current;
		}
		else { //target is in middle
			retVal = current.getNext().getElement();
			current.setNext(current.getNext().getNext());
		}
		size --;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> current = head;
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		current.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> current = head;
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		
		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		int index = -1;
		int currentIndex = 0;

		LinearNode<T> current = head;
		while(current != null && !current.getElement().equals(element)) {
			currentIndex++;
			current = current.getNext();
		}
		if (current != null) {
			return currentIndex;
		}
		return index;
	}

	@Override
	public T first() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		
		return head.getElement();
	}

	@Override
	public T last() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {

		return indexOf(target) > -1;
	}

	@Override
	public boolean isEmpty() {

		return (tail == null);
	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		LinearNode<T> current = head;
		while(current != null) {
			str.append(current.getElement().toString());
			str.append(", ");
			current = current.getNext();
		}
		if(size() > 0) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private LinearNode<T> nextNode;
		private int iterModCount;
		private boolean canRemove;

		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			return (nextNode != null);
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;
			return retVal;
		}

		@Override
		public void remove() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(!canRemove) {
				throw new IllegalStateException();
			}
			canRemove = false;

			if(size == 1) {
				head = tail = null;
			}
			else if(head.getNext() == nextNode) { //head was last returned node
				head = nextNode;
			}
			else if(nextNode == null) { //tail was last returned node
				LinearNode<T> newTail = head;
				while(newTail.getNext() != tail) {
					newTail = newTail.getNext();
				}
				newTail.setNext(null);
				tail = newTail;
			}
			else { //somewhere in middle
				LinearNode<T> current = head;
				while(current.getNext().getNext() != nextNode) {
					current = current.getNext();
				}
				current.setNext(nextNode);
			}
			size--;
			modCount++;
			iterModCount++;
		}
	}
}
