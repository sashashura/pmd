/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import net.sourceforge.pmd.lang.java.ast.InternalInterfaces.QualifierOwner;

/**
 * Method or constructor reference expression.
 *
 * <pre class="grammar">
 *
 * MethodReference ::= {@link ASTExpression Expression} "::" {@link ASTTypeArguments TypeArguments}? &lt;IDENTIFIER&gt;
 *                   | {@link ASTTypeExpression TypeExpression} "::" {@link ASTTypeArguments TypeArguments}? "new"
 *
 * </pre>
 */
public final class ASTMethodReference extends AbstractJavaExpr implements ASTPrimaryExpression, QualifierOwner, LeftRecursiveNode {

    ASTMethodReference(int id) {
        super(id);
    }


    @Override
    protected <P, R> R acceptVisitor(JavaVisitor<? super P, ? extends R> visitor, P data) {
        return visitor.visit(this, data);
    }


    @Override
    public void jjtClose() {
        super.jjtClose();
        JavaNode lhs = getChild(0);
        // if constructor ref, then the LHS is unambiguously a type.
        if (lhs instanceof ASTAmbiguousName) {
            if (isConstructorReference()) {
                setChild(new ASTTypeExpression(((ASTAmbiguousName) lhs).forceTypeContext()), 0);
            }
        } else if (lhs instanceof ASTType) {
            setChild(new ASTTypeExpression((ASTType) lhs), 0);
        }
    }

    /**
     * Returns true if this is a constructor reference,
     * e.g. {@code ArrayList::new}.
     */
    public boolean isConstructorReference() {
        return "new".equals(getImage());
    }

    /**
     * Returns the node to the left of the "::". This may be a
     * {@link ASTTypeExpression type expression}, or an
     * {@link ASTAmbiguousName ambiguous name}.
     *
     * <p>Note that if this is a {@linkplain #isConstructorReference() constructor reference},
     * then this can only return a {@linkplain ASTTypeExpression type expression}.
     */
    @NonNull
    @Override
    public ASTExpression getQualifier() {
        return (ASTExpression) getChild(0);
    }


    /**
     * Returns the explicit type arguments mentioned after the "::" if they exist.
     * Type arguments mentioned before the "::", if any, are contained within
     * the {@linkplain #getQualifier() lhs type}.
     */
    @Nullable
    public ASTTypeArguments getExplicitTypeArguments() {
        return getFirstChildOfType(ASTTypeArguments.class);
    }


    /**
     * Returns the method name, or an empty optional if this is a
     * {@linkplain #isConstructorReference() constructor reference}.
     */
    @Nullable
    public String getMethodName() {
        return getImage().equals("new") ? null : getImage();
    }
}
