function focusTextarea(event) {
    const textarea = event.currentTarget.querySelector('textarea'); // クリックされたdiv内のtextareaを取得
    textarea.focus(); // textareaにフォーカスを移動
}