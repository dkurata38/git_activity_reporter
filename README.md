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
Java8以上が必要. 以下の例は最新LTSのJava11をインストールする方法. 詳細は[こちら](https://qiita.com/seijikohara/items/56cc4ac83ef9d686fab2)
```
$ brew tap homebrew/cask-versions
$ brew cask install java11
```

3. sbtをインストールする.
Homebrewでインストールする方法と, 公式のバイナリを使ったインストールをする方法の2種類がある. 以下はHomebrewでインストールするためのコマンド. 詳細は[こちら](https://www.scala-sbt.org/1.0/docs/ja/Installing-sbt-on-Mac.html)
```
$ brew install sbt@1
```

4. `conf/application-sample.conf`を`application.conf`にリネームし, 空欄になっているところを埋める.
その際, [GitHubOauthAppの作成](https://windii.jp/study/howto/github-oauth)と, [TwitterAppの登録](https://qiita.com/kngsym2018/items/2524d21455aac111cdee)が必要になる.

callback_urlのパスは `conf/routes`に定義されているパスと一致させる.

5. アプリケーションを起動
```
$ sbt run
```

#### herokuにデプロイをする.
1. heroku CLIをインストールする.
https://devcenter.heroku.com/articles/getting-started-with-scala#set-up

2. heroku上でアプリケーションを作成をする.
好きな名前のアプリケーションを作成する.

3. heroku上で作成したアプリケーションの環境変数を作成する.

4. デプロイする
