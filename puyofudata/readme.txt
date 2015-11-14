ぷよ譜検索(http://www.cuboktahedron.sakura.ne.jp/pse/)のサイトで公開されているぷよ譜を
作成したpuyofuで再生できるJSON形式に変換するツールです。

ソースはjavascriptで、実行にはnode.jsが必要です。
Visual Studio 2015用のプロジェクトファイルになっていますが、
javascriptの実行のみであればVisual Studioは不要です。
Visual Studioを使うとnode.jsのデバッグが行えます。(「Node.js Tools for Visual Studio」が必要)

実行方法は以下の通り。

node puyosimu.js <ぷよ譜エンコード文字列> <名前>

<ぷよ譜エンコード文字列>は、ぷよ譜検索サイトのリンクのURLの「_」より右側の文字列です。

<ぷよ譜エンコード文字列>と<名前>をぷよ譜検索サイトから抜き出した一覧は、
「list.txt」にあります。

変換を実行済みの全2802件のデータは、「out」フォルダに格納しています。
