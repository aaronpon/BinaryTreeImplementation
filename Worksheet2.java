public class Worksheet2 implements Worksheet2Interface {

	// Exercise 1

	public static Tree<Integer> negateAll(Tree<Integer> t) {
		if (t.isEmpty()) {
			return t;
		} else {
			return new Tree<Integer>(-t.getValue(), negateAll(t.getLeft()), negateAll(t.getRight()));
		}
	}

	// Exercise 2

	public static boolean allPositive(Tree<Integer> a) {
		if (a.isEmpty())
			return true;
		else
			/*
			 * Note that a.getValue *must* be first, as this is taking advantage of short
			 * circuiting.
			 *
			 * Alternatively an if statement could be used.
			 */
			return a.getValue() >= 0 && allPositive(a.getLeft()) && allPositive(a.getRight());

	}

	// Exercise 3
	// Return the level of a given node value with a helper method
	private static int levelHelper(Tree<Integer> t, int x, int level) {
		if (t.isEmpty())
			return 0;
		if (t.getValue() == x) {
			return level;
		}
		int subLevel = levelHelper(t.getLeft(), x, level + 1);
		if (subLevel != 0)
			return subLevel;
		else
			return levelHelper(t.getRight(), x, level + 1);

	}

	public static int levelH(Tree<Integer> a, int x) {
		return levelHelper(a, x, 1);
	}

	// Exercise 3
	// Returns the level of a given node value without a helper method
	public static int level(Tree<Integer> t, int x) {
		if (t.isEmpty())
			return 0;
		if (t.getValue() == x)
			return 1;
		else {
			int sublevel = Math.max(level(t.getLeft(), x), level(t.getRight(), x));
			if (sublevel == 0)
				return sublevel;
			else
				return 1 + sublevel;

		}
	}

	// Exercise 4
	/**
	 * Postorder means visiting: - left - right - this node
	 *
	 * However if we visit the left first, then we need to 'append' new elements to
	 * the list when we visit right. Append requires searching through the list to
	 * reach the end. So, the resulting code `postorderSlow' takes O(n^2) time.
	 *
	 * To avoid this, created a helper function with an accumulating parameter that
	 * holds the list obtained from the remaining tree. This works in O(n) time.
	 */

	public static <E> List<E> postorderSlow(Tree<E> t) {
		if (t.isEmpty())
			return new List<>();
		else
			return append(postorder(t.getLeft()),
					append(postorder(t.getRight()), new List<E>(t.getValue(), new List<E>())));
	}

	// append creates a new list by appending two given lists
	// It is generic in the element type E.

	public static <E> List<E> append(List<E> a, List<E> b) {
		if (a.isEmpty())
			return b;
		else
			return new List<E>(a.getHead(), append(a.getTail(), b));
	}

	public static <E> List<E> postorder(Tree<E> t) {
		return postorder(t, new List<E>());
	}

	// Helper function for postorder(Tree).
	// add the postorder traversal list of t
	// at the front of ls

	private static <E> List<E> postorder(Tree<E> t, List<E> ls) {
		if (t.isEmpty())
			return ls;
		else
			return postorder(t.getLeft(), postorder(t.getRight(), new List<E>(t.getValue(), ls)));
	}

	// Exercise 5

	/**
	 * @param a - a tree
	 * @return boolean value indicating whether a is a binary search tree.
	 */

	public static boolean isSearchTree(Tree<Integer> a) {
		if (a.isEmpty())
			return true;
		else
			return isSearchTree(a, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	// Helper function for isSearchTree.
	// Returns a boolean to say whether `a' is a search tree whose
	// values are within the range between `lowerBound' and `upperBound'.

	private static boolean isSearchTree(Tree<Integer> a, int lowerBound, int upperBound) {
		if (a.isEmpty())
			return true;
		else {
			int newLower = Math.max(a.getValue(), lowerBound);
			int newUpper = Math.min(a.getValue(), upperBound);

			return a.getValue() < upperBound && a.getValue() > lowerBound
					&& isSearchTree(a.getLeft(), lowerBound, newUpper)
					&& isSearchTree(a.getRight(), newLower, upperBound);
		}
	}

	// Exercise 6

	public static void printDescending(Tree<Integer> a) {
		if (a.isEmpty())
			return;
		else {
			printDescending(a.getRight());
			System.out.println(a.getValue());
			printDescending(a.getLeft());
		}
	}

	// Exercise 7

	/**
	 * You should: - handle empty trees - never looks at left - never compares
	 * values, i.e. a < right, as no need if it's search tree. - returns right value
	 * as soon as found
	 *
	 */
	public static int max(Tree<Integer> a) {
		if (a.isEmpty())
			throw new IllegalStateException("empty tree given, no max value exists");
		else {
			Tree<Integer> right = a.getRight();

			if (right.isEmpty())
				return a.getValue();
			else
				return max(right);
		}
	}

	// Exercise 8

	/**
	 * Deletes the value 'x' from the given tree, if it exists, and returns the new
	 * Tree.
	 *
	 * Otherwise, the original tree will be returned.
	 */
	public static Tree<Integer> delete(Tree<Integer> a, int x) {
		if (a.isEmpty())
			return a;
		else {
			int value = a.getValue();
			Tree<Integer> left = a.getLeft();
			Tree<Integer> right = a.getRight();

			if (x > value) {
				Tree<Integer> newRight = delete(right, x);
				return new Tree<Integer>(a.getValue(), left, newRight);
			} else if (x < value) {
				Tree<Integer> newLeft = delete(left, x);
				return new Tree<Integer>(a.getValue(), newLeft, right);
			} else {
				// The deletion!
				if (left.isEmpty())
					return right;
				else if (right.isEmpty())
					return left;
				/*
				 * Neither subtree is empty. So one subtree is going to lose a node. Let it be
				 * the left. We delete the maximum from the left subtree.
				 */

				else {
					int predecessor = max(left);
					return new Tree<Integer>(predecessor, delete(left, predecessor), right);
				}
			}
		}
	}

	// For convenience, we define a static function for height

	private static <E> int height(Tree<E> t) {
		return t.getHeight();
	}

	// Exercise 9

	/**
	 * @param t - a tree
	 * @return boolean value indicating whether t is height balanced
	 */
	public static <E> boolean isHeightBalanced(Tree<E> t) {
		if (t.isEmpty())
			return true;
		else {
			int lh = height(t.getLeft());
			int rh = height(t.getRight());
			if ((Math.abs(lh - rh)) <= 1) {
				return (isHeightBalanced(t.getLeft()) && isHeightBalanced(t.getRight()));
			} else
				return false;
		}
	}

	// Exercise 10

	/**
	 * @param x - an integer
	 * @param t - a tree, assumed to be an AVL tree
	 * @return a tree obtained by inserting x in t, an AVL tree
	 */
	public static Tree<Integer> insertHB(Tree<Integer> t, int x) {
		if (t.isEmpty())
			return new Tree<Integer>(x);
		else if (x < t.getValue()) {
			Tree<Integer> newLeft = insertHB(t.getLeft(), x);
			return rebalanceForLeft(new Tree<Integer>(t.getValue(), newLeft, t.getRight()));
		} else if (x > t.getValue()) {
			Tree<Integer> newRight = insertHB(t.getRight(), x);
			return rebalanceForRight(new Tree<Integer>(t.getValue(), t.getLeft(), newRight));
		} else
			return t;
	}

	/**
	 * @param a - a tree, assumed to be an AVL tree
	 * @param x - a value that occurs in a
	 * @return the tree obtained by deleting x from a, an AVL tree
	 */
	public static <E> Tree<Integer> deleteHB(Tree<Integer> a, int x) {
		if (a.isEmpty())
			throw new IllegalArgumentException("deleteHB: value is not in the tree");
		else {
			int value = a.getValue();
			Tree<Integer> left = a.getLeft();
			Tree<Integer> right = a.getRight();

			if (x > value) {
				Tree<Integer> newRight = deleteHB(right, x);
				return rebalanceForLeft(new Tree<Integer>(a.getValue(), left, newRight));
			} else if (x < value) {
				Tree<Integer> newLeft = deleteHB(left, x);
				return rebalanceForRight(new Tree<Integer>(a.getValue(), newLeft, right));

			} else {
				if (left.isEmpty()) {
					return right;
				} else if (right.isEmpty()) {
					return left;
				} else {
					int predecessor = max(left);
					return rebalanceForRight(new Tree<Integer>(predecessor, deleteHB(left, predecessor), right));
				}
			}
		}
	}

	// rebalanceForLeft is called when the left subtree of t may have
	// grown taller by one notch.
	// If it is indeed taller than the right subtree by two notches,
	// return a height-balanced version of t using single or double rotations.
	// The subtrees of t are assumed to be already height-balanced and
	// no effort is made to rebalance them.

	private static <E> Tree<E> rebalanceForLeft(Tree<E> t) {
		if (height(t.getLeft()) <= height(t.getRight()) + 1)
			return t;
		else {
			Tree<E> l = t.getLeft(); // height h+2
			Tree<E> r = t.getRight(); // height h
			Tree<E> ll = l.getLeft(); // height h+1?
			Tree<E> lr = l.getRight(); // height h+1?
			if (height(ll) > height(r)) {
				// LL rotation - single
				return new Tree<E>(l.getValue(), ll, new Tree<E>(t.getValue(), lr, r));
			} else {
				assert height(lr) > height(r);
				// LR rotation - double
				return new Tree<E>(lr.getValue(), new Tree<E>(l.getValue(), l.getLeft(), lr.getLeft()),
						new Tree<E>(t.getValue(), lr.getRight(), t.getRight()));
			}
		}
	}

	private static <E> Tree<E> rebalanceForRight(Tree<E> t) {
		if (height(t.getRight()) <= height(t.getLeft()) + 1)
			return t;
		else {
			Tree<E> l = t.getLeft(); // height h
			Tree<E> r = t.getRight(); // height h+2
			Tree<E> rl = r.getLeft(); // height h+1?
			Tree<E> rr = r.getRight(); // height h+1?
			if (height(rr) > height(l)) {
				// RR rotation - single
				return new Tree<E>(r.getValue(), new Tree<E>(t.getValue(), l, rl), rr);
			} else {
				assert height(rl) > height(l);
				// RL rotation - double
				return new Tree<E>(rl.getValue(), new Tree<E>(t.getValue(), t.getLeft(), rl.getLeft()),
						new Tree<E>(r.getValue(), rl.getRight(), r.getRight()));
			}
		}
	}
}
