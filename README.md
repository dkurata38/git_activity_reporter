# git_activity_reporter
Web上でGitでの活動ログを確認したり, 簡単にSNSにシェアできるようにするツール.

## 環境構築
### ローカル開発PCでの起動
#### intelliJ IDEAで動かす
1. ソースをクローンする.
```
$ git clone https://github.com/dkurata38/git_activity_reporter.git
```

2. intelliJ IDEAをインストールする.
インストールは[こちら](https://www.jetbrains.com/idea/)から

3. intelliJ IDEAにScalaプラグインとSBTプラグインをインストールする.
intelliJ起動後, Configure>Pluginsを順に選択し, ScalaプラグインとSBTプラグインを検索し, インストールする.

4. intelliJ IDEAを再起動したら, Import Projectを選択肢, 1.でクローンしたソースのディレクトリを選択する.

5. etc... 


#### コンソール上で動かす
1. ソースをクローンする.
```
$ git clone https://github.com/dkurata38/git_activity_reporter.git
```

2. Javaをインストールする.
Java8以上が必要. インストール方法は[こちら](https://qiita.com/seijikohara/items/56cc4ac83ef9d686fab2)

3. sbtをインストールする.
Homebrewでインストールする方法と, 公式のバイナリを使ったインストールをする方法の2種類がある. 詳細は[こちら](https://www.scala-sbt.org/1.0/docs/ja/Installing-sbt-on-Mac.html)

4. etc...

5. アプリケーションを起動
```
$ sbt run
```
