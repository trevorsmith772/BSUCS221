import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * 
 * Double-linked node implementation of IndexedUnsortedList
 * @author TrevorSmith CS221-3 Spring 2020
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head, tail;
	private int size, modCount;

	/** Initialize a new empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(head);
		newNode.setPrev(null);
		head = newNode;
		if(tail == null) {	//list is empty
			tail = head;
		}
		else {	//not empty
			head.getNext().setPrev(newNode);
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(null);
		if(head == null) {	//list is empty
			head = newNode;
			newNode.setPrev(null);
		}
		else {	//not empty
			newNode.setPrev(tail);
			tail.setNext(newNode);
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
		LinearNode<T> targetNode = head;
		while(targetNode != null && !targetNode.getElement().equals(target)) {
			targetNode = targetNode.getNext();
		}
		if(targetNode == null) {
			throw new NoSuchElementException();
		}
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(targetNode.getNext());
		newNode.setPrev(targetNode);
		targetNode.setNext(newNode);
		if(targetNode == tail) {
			tail = newNode;
		}
		else {
			newNode.getNext().setPrev(newNode);
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
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		if(head == null) {	//list is empty
			head = tail = newNode;
		}
		else if(current != null && index != 0){	//adding in middle of list
			newNode.setNext(current);
			newNode.setPrev(current.getPrev());
			newNode.getPrev().setNext(newNode);
			newNode.getNext().setPrev(newNode);
		}
		else if(current != null && index == 0) {	//adding new head of non-empty list
			newNode.setNext(head);
			head.setPrev(newNode);
			head = newNode;
		}
		else {	//adding new tail of non-empty list
			newNode.setPrev(tail);
			tail.setNext(newNode);
			tail = newNode;
		}

		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		if(size == 1) {
			head = tail = null;
		}
		else { 
			head = head.getNext();
			head.setPrev(null);
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
		LinearNode<T> newTail = tail.getPrev();
		tail = newTail;
		if(size == 1) {
			head = null;
		}
		else {
			newTail.setNext(null);
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<T> current = head;
		while(current != null && !current.getElement().equals(element)) {
			current = current.getNext();
		}
		if(current == null) {
			throw new NoSuchElementException();
		}
		T retVal = current.getElement();
		if (size() == 1) { //only node
			head = tail = null;
		}
		else if (current == head) { //first node
			head = current.getNext();
			head.setPrev(null);
		}
		else if (current == tail) { //last node
			tail = current.getPrev();
			tail.setNext(null);
		}
		else { //somewhere in the middle
			current.getPrev().setNext(current.getNext());
			current.getNext().setPrev(current.getPrev());
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> current = head;
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		T retVal = current.getElement();
		if (size() == 1) { //only node
			head = tail = null;
		}
		else if (current == head) { //first node
			head = current.getNext();
			head.setPrev(null);
		}
		else if (current == tail) { //last node
			tail = current.getPrev();
			tail.setNext(null);
		}
		else { //somewhere in the middle
			current.getPrev().setNext(current.getNext());
			current.getNext().setPrev(current.getPrev());
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= size) {
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
		if(index < 0 || index >= size) {
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

		return (size == 0);
	}

	@Override
	public int size() {

		return size;
	}

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

		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {

		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {

		return new DLLIterator(startingIndex);
	}

	/** ListIterator for IUDoubleLinkedList */
	private class DLLIterator implements ListIterator<T> {
		private LinearNode<T> nextNode;
		private int nextIndex;
		private int iterModCount;
		private LinearNode<T> lastReturned;

		/* Initialize iterator in front of first node */
		public DLLIterator() {
			this(0);
		}

		/** 
		 * Initialize iterator in front of startingIndex
		 * @param startingIndex index of the element that would be next
		 */
		public DLLIterator(int startingIndex) {
			if(startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			iterModCount = modCount;
			nextNode = head;
			for(int i = 0; i < startingIndex; i++) {
				nextNode = nextNode.getNext();
			}
			nextIndex = startingIndex;
			lastReturned = null;
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
			lastReturned = nextNode;
			nextNode = nextNode.getNext();
			nextIndex++;

			return retVal;
		}

		@Override
		public boolean hasPrevious() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		@Override
		public T previous() {
			if(!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if(nextNode == null) {
				nextNode = tail;
			}
			else {
				nextNode = nextNode.getPrev();
			}
			lastReturned = nextNode;
			nextIndex--;
			return nextNode.getElement();
		}

		@Override
		public int nextIndex() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextIndex - 1);
		}

		@Override
		public void remove() {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(lastReturned == null) { //cant remove
				throw new IllegalStateException();
			}
			if(head == tail) { //Only node
				head = tail = null;
			}
			else if(lastReturned == tail) {	//remove tail
				tail = tail.getPrev();
				tail.setNext(null);
			}
			else if(lastReturned == head) {	//remove head
				head = head.getNext();
				head.setPrev(null);
			}
			else {	//remove middle
				if(lastReturned == nextNode) {	//previous was called
					nextNode.getPrev().setNext(nextNode.getNext());
					nextNode = nextNode.getNext();
					nextNode.setPrev(nextNode.getPrev().getPrev());
				}
				else {	//next was called
					nextNode.setPrev(nextNode.getPrev().getPrev());
					nextNode.getPrev().setNext(nextNode);
				}
				nextIndex--;
			}
			lastReturned = null;
			size--;
			iterModCount++;
			modCount++;
		}

		@Override
		public void set(T e) {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if(lastReturned == null) {
				throw new IllegalStateException();
			}
			lastReturned.setElement(e);
			lastReturned = null;
			iterModCount++;
			modCount++;
		}

		@Override
		public void add(T e) {
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			LinearNode<T> newNode = new LinearNode<T>(e);
			if(head == null) {	//only node
				head = tail = newNode;
			}
			else if(nextNode == null) { //new tail
				newNode.setPrev(tail);
				tail.setNext(newNode);
				tail = newNode;
			}
			else if(nextNode == head) { //new head
				newNode.setNext(head);
				head.setPrev(newNode);
				head = newNode;
			}
			else {	//new middle element
				newNode.setNext(nextNode);
				newNode.setPrev(nextNode.getPrev());
				nextNode.setPrev(newNode);
				newNode.getPrev().setNext(newNode);
			}
			nextIndex++;
			size++;
			modCount++;
			iterModCount++;
		}
	}
}