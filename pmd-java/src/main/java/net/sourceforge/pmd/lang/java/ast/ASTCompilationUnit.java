/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import net.sourceforge.pmd.annotation.InternalApi;
import net.sourceforge.pmd.lang.ast.NodeStream;
import net.sourceforge.pmd.lang.ast.RootNode;
import net.sourceforge.pmd.lang.ast.impl.GenericNode;
import net.sourceforge.pmd.lang.java.symbols.table.JSymbolTable;
import net.sourceforge.pmd.lang.java.typeresolution.ClassTypeResolver;

// FUTURE Change this class to extend from SimpleJavaNode, as TypeNode is not appropriate (unless I'm wrong)
public final class ASTCompilationUnit extends AbstractJavaTypeNode implements JavaNode, GenericNode<JavaNode>, RootNode {

    private ClassTypeResolver classTypeResolver;
    private List<Comment> comments;
    private Map<Integer, String> noPmdComments = Collections.emptyMap();

    ASTCompilationUnit(int id) {
        super(id);
    }

    public List<Comment> getComments() {
        return comments;
    }

    void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    @Override
    protected <P, R> R acceptVisitor(JavaVisitor<? super P, ? extends R> visitor, P data) {
        return visitor.visit(this, data);
    }


    @Nullable
    public ASTPackageDeclaration getPackageDeclaration() {
        return AstImplUtil.getChildAs(this, 0, ASTPackageDeclaration.class);
    }

    @Override
    public @NonNull ASTCompilationUnit getRoot() {
        return this;
    }

    /**
     * Returns the package name of this compilation unit. If there is no
     * package declaration, then returns the empty string.
     */
    @NonNull
    public String getPackageName() {
        ASTPackageDeclaration pack = getPackageDeclaration();
        return pack == null ? "" : pack.getPackageNameImage();
    }

    /**
     * Returns the type declarations declared in this compilation unit.
     * This may be empty if this a package-info.java, or a modular
     * compilation unit.
     */
    public NodeStream<ASTAnyTypeDeclaration> getTypeDeclarations() {
        return children(ASTAnyTypeDeclaration.class);
    }


    @InternalApi
    @Deprecated
    public ClassTypeResolver getClassTypeResolver() {
        return classTypeResolver;
    }


    @Override
    public @NonNull JSymbolTable getSymbolTable() {
        assert symbolTable != null : "Symbol table wasn't set";
        return symbolTable;
    }


    @InternalApi
    @Deprecated
    public void setClassTypeResolver(ClassTypeResolver classTypeResolver) {
        this.classTypeResolver = classTypeResolver;
    }

    @Override
    public Map<Integer, String> getNoPmdComments() {
        return noPmdComments;
    }

    void setNoPmdComments(Map<Integer, String> noPmdComments) {
        this.noPmdComments = noPmdComments;
    }
}
