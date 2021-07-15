package com.github.bhanuunrivalled.checktest;


import com.github.bhanuunrivalled.checktest.backend.GraphBuilder;
import com.intellij.debugger.engine.JavaValue;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.ui.impl.watch.ValueDescriptorImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValuePlace;
import com.sun.jdi.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.MessageFormat;

public class TestCompositeNode implements XCompositeNode {
    private static final Logger LOGGER = Logger.getInstance(TestCompositeNode.class);
    private final Logger LOG = Logger.getInstance(getClass());

    @Override
    public void addChildren(@NotNull final XValueChildrenList children, final boolean last) {
        LOG.info("in add children ");
        for (int i = 0; i < children.size(); i++) {
            final JavaValue value = (JavaValue) children.getValue(i);
            this.checkValue(value);
        }

    }

    void checkValue(final JavaValue jValue) {
        final String variableName = jValue.getName();
        LOG.info("variable name is  " + variableName);
        if (!variableName.equals("args")) {
            LOG.info(MessageFormat.format("inside if statement {0}", variableName));
            try {
                final Value calculatedValue = jValue.getDescriptor().calcValue(jValue.getEvaluationContext());
                String actualGraph = new GraphBuilder().generateDOT(calculatedValue);
                LOG.info(MessageFormat.format(" actual Graph for   {0} is {1}", variableName, actualGraph));
            } catch (EvaluateException e) {
                e.printStackTrace();
            }
//            ValueDescriptorImpl descriptor = ((JavaValue) jValue).getDescriptor();
//            jValue.computePresentation(new NOOPXValueNode(), XValuePlace.TREE);
//            if (descriptor.isValueReady()) {
//                Value nodeValue = descriptor.getValue();
//                String actualGraph = new GraphBuilder().generateDOT(nodeValue);
//                LOG.info(MessageFormat.format(" actual Graph for   {0} is {1}", variableName, actualGraph));
//            }
        }
    }


    @Override
    public void tooManyChildren(final int remaining) {
        LOGGER.debug("tooManyChildren called!");
    }

    @Override
    public void setAlreadySorted(final boolean alreadySorted) {
        LOGGER.debug("setAlreadySorted called!");
    }

    @Override
    public void setErrorMessage(@NotNull final String errorMessage) {
        LOGGER.warn(errorMessage);
    }

    @Override
    public void setErrorMessage(@NotNull final String errorMessage, @Nullable final XDebuggerTreeNodeHyperlink link) {
        LOGGER.warn(errorMessage);
    }

    @Override
    public void setMessage(
            @NotNull final String message,
            @Nullable final Icon icon,
            @NotNull final SimpleTextAttributes attributes,
            @Nullable final XDebuggerTreeNodeHyperlink link) {
        LOGGER.debug(message);
    }
}