package com.simscale.main.model;

public class LogLinkedList {
	private String id;
	private Node root;
	private boolean rootFound = false;

	public LogLinkedList(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void addCall(CallEntry callEntry) {
		Node current = new Node( callEntry.getCaller(), callEntry );
		if(root == null) {
			root = current;
			return;
		}
		Node parent = root;
		while(parent != null && !parent.id.equals( current.id )) {
			if(parent.id.equals( current.call.getCallee()) || current.call.getCaller().equals( "null" ) ){
				Node aux = parent;
				if(parent == root || current.call.getCaller().equals( "null" ))
					root = current;
				parent = current;
				parent.next = aux;
				rootFound = current.call.getCaller().equals( "null" );

				return;
			} else if(current.id.equals( parent.id )){
				current.next = parent.next;
				parent.next = current;
			} else if(parent.next == null) {
				parent.next = current;
			}
			parent =	parent.next;
		}
	}

	public boolean isRootFound(){
		return rootFound;
	}

	class Node {
		private String id;
		private CallEntry call;
		Node next;
		Node(String id, CallEntry call){
			this.id = id;
			this.call = call;
		}
	}

	public void print(){
		Node p = root;
		while(p != null){
			System.out.println(p.id + " -> " + p.call.getCallee());
			p = p.next;
		}
	}
}
