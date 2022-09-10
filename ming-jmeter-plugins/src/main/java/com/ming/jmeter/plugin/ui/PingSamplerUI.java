package com.ming.jmeter.plugin.ui;

import com.ming.jmeter.plugin.PingSampler;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PingSamplerUI extends AbstractSamplerGui {
    private static final Logger logger = LoggerFactory.getLogger(PingSamplerUI.class);
    private static final String LABEL_NAME = "ping";
    private static final String DEFAULT_TARGET = "127.0.0.1";

    private final JLabeledTextField targetField = new JLabeledTextField("target");
    private final JSyntaxTextArea textMessage = new JSyntaxTextArea(10, 50);
    private final JLabel textArea = new JLabel("Message");
    private final JTextScrollPane textPanel = new JTextScrollPane(textMessage);

    public PingSamplerUI() {
        super();
        this.init();
    }

    @Override
    public String getLabelResource() {
        return LABEL_NAME;
    }

    @Override
    public String getStaticLabel() {
        return LABEL_NAME;
    }

    @Override
    public TestElement createTestElement() {
        PingSampler sampler = new PingSampler();
        this.setupSamplerProperties(sampler);
        return sampler;
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        PingSampler sampler = (PingSampler) element;
        this.targetField.setText(sampler.getTarget());
    }

    @Override
    public void modifyTestElement(TestElement element) {
        PingSampler sampler = (PingSampler) element;
        this.setupSamplerProperties(sampler);
    }

    private void init() {
        logger.info("Init PingSamplerUI...");
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);
        JPanel mainPanel = new VerticalPanel();
        add(mainPanel, BorderLayout.CENTER);

        JPanel DPanel = new JPanel();
        DPanel.setLayout(new GridLayout(4, 2));
        DPanel.add(targetField);

        JPanel ControlPanel = new VerticalPanel();
        ControlPanel.add(DPanel);
        ControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "参数"));
        mainPanel.add(ControlPanel);

        /**输出**/
        JPanel ContentPanel = new VerticalPanel();
        JPanel messageContentPanel = new JPanel(new BorderLayout());
        messageContentPanel.add(this.textArea, BorderLayout.NORTH);
        messageContentPanel.add(this.textPanel, BorderLayout.CENTER);
        ContentPanel.add(messageContentPanel);
        ContentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Content"));
        mainPanel.add(ContentPanel);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        this.targetField.setText(DEFAULT_TARGET);
    }

    private void setupSamplerProperties(PingSampler sampler) {
        this.configureTestElement(sampler);
        sampler.setTarget(this.targetField.getText());
    }
}
