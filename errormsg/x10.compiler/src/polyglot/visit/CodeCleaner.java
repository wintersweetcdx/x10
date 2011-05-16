/*
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2006 Polyglot project group, Cornell University
 * 
 */

package polyglot.visit;

import java.util.*;

import polyglot.ast.*;
import polyglot.types.Name;
import polyglot.util.CollectionUtil; import x10.util.CollectionFactory;

/**
 * The <code>CodeCleaner</code> runs over the AST and performs some trivial
 * dead code elimination, while flattening blocks wherever possible.
 **/
public class CodeCleaner extends NodeVisitor {

  protected NodeFactory nf;
  protected AlphaRenamer alphaRen;

  /**
   * Creates a visitor for cleaning code.
   *
   * @param nf  The node factory to be used when generating new nodes.
   **/
  public CodeCleaner(NodeFactory nf) {
    this.nf = nf;
    this.alphaRen = new AlphaRenamer();
  }

  public Node leave( Node old, Node n, NodeVisitor v ) {
    if ( !(n instanceof Block || n instanceof Labeled) ) {
      return n;
    }

    // If we have a labeled block consisting of just one statement, then
    // flatten the block and label the statement instead.  We also flatten
    // labeled blocks when there is no reference to the label within the
    // block.
    if ( n instanceof Labeled ) {
      Labeled l = (Labeled)n;
      if ( !(l.statement() instanceof Block) ) {
        return n;
      }

      Block b = (Block)l.statement();
      if ( b.statements().size() != 1 ) {
	if ( labelRefs(b).contains(l.labelNode().id()) ) {
	  return n;
	}

	// There's no reference to the label within the block, so flatten and
	// clean up dead code.
        return nf.Block( b.position(), clean(flattenBlock(b)) );
      }

      // Alpha-rename local decls in the block that we're flattening.
      b = (Block)b.visit(alphaRen);
      return nf.Labeled( l.position(), l.labelNode(),
                         (Stmt)b.statements().get(0) );
    }

    // Flatten any blocks that may be contained in this one, and clean up dead
    // code.
    Block b = (Block)n;
    List<Stmt> stmtList = clean(flattenBlock(b));

    if ( b instanceof SwitchBlock ) {
      return nf.SwitchBlock( b.position(), stmtList );
    }

    return nf.Block( b.position(), stmtList );
  }

  /**
   * Turns a Block into a list of Stmts.
   **/
  protected List<Stmt> flattenBlock( Block b ) {
    List<Stmt> stmtList = new LinkedList<Stmt>();
    for (Stmt stmt : b.statements()) {
      if ( stmt instanceof Block ) {
	// Alpha-rename local decls in the block that we're flattening.
	stmt = (Stmt)stmt.visit(alphaRen);
        stmtList.addAll( ((Block)stmt).statements() );
      } else {
        stmtList.add( stmt );
      }
    }

    return stmtList;
  }

  /**
   * Performs some trivial dead code elimination on a list of statements.
   **/
  protected List<Stmt> clean( List<Stmt> l ) {
    List<Stmt> stmtList = new LinkedList<Stmt>();
    for (Stmt stmt : l) {
      stmtList.add( stmt );

      if ( stmt instanceof Branch || stmt instanceof Return
           || stmt instanceof Throw ) {
	return stmtList;
      }
    }

    return l;
  }

  /**
   * Traverses a Block and determines the set of label references.
   **/
  protected Set<Name> labelRefs( Block b ) {
    final Set<Name> result = CollectionFactory.newHashSet();
    b.visit( new NodeVisitor() {
	public Node leave( Node old, Node n, NodeVisitor v ) {
	  if ( n instanceof Branch ) {
	    result.add( ((Branch)n).labelNode().id() );
	  }

	  return n;
	}
      } );

    return result;
  }
}