package typingapp.ui;

import javax.swing.*;
import java.awt.*;

public class TypingGameUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private String difficulty;
    private Image backgroundImage;

    // UIコンポーネント
    private JLabel timerLabel;
    private JLabel kanjiLabel;
    private JLabel hiraganaLabel;
    private JTextPane typingPane;
    private JTextField inputField;
    private SushiPanel sushiPanel;

    public TypingGameUI(String difficulty) {
        this.difficulty = difficulty;

        // 1. 難易度に応じた背景画像をロード
        loadBackgroundImage(difficulty);

        // 2. パネル自体の設定（透明化＆レイアウト）
        setOpaque(false);
        setLayout(new BorderLayout());

        // 3. 各UIエリアの構築
        initUI();
    }

    /**
     * 難易度に応じた背景画像を読み込む
     */
    public void loadBackgroundImage(String difficulty) {
        this.difficulty = difficulty;
        String imagePath = switch (difficulty) {
            case "初級" -> "/image.gameForBeginner.jpg";
            case "中級" -> "/image.gameForMidium.jpg";
            case "上級" -> "/image.gameForPro.jpg";
            default    -> "";
        };

        var resource = getClass().getResource(imagePath);
        if (resource != null) {
            this.backgroundImage = new ImageIcon(resource).getImage();
            repaint();
        } else {
            System.err.println("背景画像が見つかりません: " + imagePath);
        }
    }

    /**
     * UIコンポーネントの配置と背景透過処理
     */
    private void initUI() {
        // --- 上部エリア（残り時間）---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false); // ★背景を透過

        timerLabel = new JLabel("ゲーム残り時間 : 60秒");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        timerLabel.setForeground(Color.BLACK); // 背景に合わせて視認性の良い色に調整
        topPanel.add(timerLabel);

        // --- 中央エリア（問題文・入力表示）---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false); // ★背景を透過

        // つやべた（ひらがな）
        hiraganaLabel = new JLabel("つやべた", SwingConstants.CENTER);
        hiraganaLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        hiraganaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ツヤベタ（漢字・メイン表示）
        kanjiLabel = new JLabel("ツヤベタ", SwingConstants.CENTER);
        kanjiLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        kanjiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ローマ字・リアルタイム入力プレビュー（JTextPane）
        typingPane = new JTextPane();
        typingPane.setEditable(false);
        typingPane.setOpaque(false); // ★背景を透過
        typingPane.setFont(new Font("Monospaced", Font.BOLD, 26));
        typingPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        // テキストを中央揃えにする設定
        javax.swing.text.SimpleAttributeSet centerAttr = new javax.swing.text.SimpleAttributeSet();
        javax.swing.text.StyleConstants.setAlignment(centerAttr, javax.swing.text.StyleConstants.ALIGN_CENTER);
        typingPane.getStyledDocument().setParagraphAttributes(0, typingPane.getStyledDocument().getLength(), centerAttr, false);

        // キー入力フィールド
        inputField = new JTextField(20);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 24));
        inputField.setMaximumSize(new Dimension(500, 40));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // コンポーネントを垂直配置（余白の追加）
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(hiraganaLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(kanjiLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(typingPane);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(inputField);

        // --- 下部エリア（寿司パネル）---
        sushiPanel = new SushiPanel(difficulty);
        sushiPanel.setOpaque(false); // ★背景を透過
        sushiPanel.setPreferredSize(new Dimension(800, 100));

        // メインパネルに追加
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(sushiPanel, BorderLayout.SOUTH);
    }

    /**
     * 背景画像の描画処理
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 難易度ごとの背景画像を画面全体に描画
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // --- 各コンポーネントへのアクセサ（コントローラー等から使用） ---
    public JLabel getTimerLabel() { return timerLabel; }
    public JLabel getKanjiLabel() { return kanjiLabel; }
    public JLabel getHiraganaLabel() { return hiraganaLabel; }
    public JTextPane getTypingPane() { return typingPane; }
    public JTextField getInputField() { return inputField; }
    public SushiPanel getSushiPanel() { return sushiPanel; }
    public String getDifficulty() { return difficulty; }
}