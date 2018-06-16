/**
 *  Copyright (c) 2015-present, Jim Kynde Meyer
 *  All rights reserved.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 */
package com.intellij.lang.jsgraphql.v1.ide.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.jsgraphql.v1.psi.JSGraphQLElementType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JSGraphQLFoldingBuilder implements FoldingBuilder {

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        final ASTNode first = node.getFirstChildNode();
        final ASTNode last = node.getLastChildNode();
        if (first != null && last != null) {
            return first.getText() + "..." + last.getText();
        }
        return "{...}";
    }

    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        List<FoldingDescriptor> list = new ArrayList<>();
        buildFolding(node, list);
        FoldingDescriptor[] descriptors = new FoldingDescriptor[list.size()];
        return list.toArray(descriptors);
    }

    private static void buildFolding(ASTNode node, List<FoldingDescriptor> list) {
        boolean isBlock = node.getElementType() instanceof JSGraphQLElementType;
        if (isBlock && !node.getTextRange().isEmpty()) {
            final TextRange range = node.getTextRange();
            list.add(new FoldingDescriptor(node, range));
        }
        for (ASTNode child : node.getChildren(null)) {
            buildFolding(child, list);
        }
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}