#include "scoretable.h"

#include <fstream>
#include <string>
#include <QDebug>

ScoreTable::ScoreTable(QWidget* parent)
    : QWidget(parent)
{
    this->setWindowTitle("Score table");
    QImage backGround(":/img/bestScore.jpg");
    this->resize(backGround.width(),backGround.height());
    this->setFixedSize(backGround.width(),backGround.height());

    brush = new QBrush;
    palette = new QPalette;
    brush->setTextureImage(backGround);
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);

    layout = new QVBoxLayout;
    QFont font;
    font.setPointSize(15);
    font.setBold(true);
    Score score;
    QVector<PlayerStats> players = score.getPlayers();
    for (int i = 0; i < players.size(); ++i)
    {
        std::string res = players[i].name + " " + std::to_string(players[i].score);
        QLabel* label = new QLabel(res.c_str());
        label->setFont(font);
        layout->addWidget(label);
        topList.push_back(label);
    }
    layout->setAlignment(Qt::AlignCenter);
    setLayout(layout);
}

ScoreTable::~ScoreTable() = default;

void ScoreTable::updateScore()
{
    Score score;
    QVector<PlayerStats> players = score.getPlayers();
    for (int i = 0; i < topList.size(); ++i)
    {
        std::string res = players[i].name + " " + std::to_string(players[i].score);
        topList[i]->setText(res.c_str());
    }
}

void ScoreTable::keyPressEvent(QKeyEvent * event)
{
    int key = event->key();
    if (key == Qt::Key_Escape)
    {
        this->close();
        emit sTable();
    }
}
