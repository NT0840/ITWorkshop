let parentRowCount = 0;
let childRowCounts = {}; // 親要素ごとの子要素カウンタ
//const parentIndex = 0;
//childRowCounts[parentIndex] = 0;

function addParentRow() {
  const parentContainer = document.querySelector('.parent-container');
  const parentRow = document.createElement('div');
  parentRow.classList.add('parent-row');

  const parentInputGroup = document.createElement('div');
  parentInputGroup.classList.add('parent-input-group');

  const orderLabel = document.createElement('label');
  orderLabel.setAttribute('for', `parent-order-${parentRowCount}`);
  orderLabel.textContent = '作成順';
  const orderInput = document.createElement('input');
  orderInput.type = 'text';
  orderInput.min = '1';
  orderInput.max = '999';
  orderInput.value = `${parentRowCount + 1}`;
  orderInput.oninput = function() {
    this.value = this.value.replace(/[^0-9]/g, ''); // 半角数字以外の入力を制限
  };
  orderInput.id = `parent-order-${parentRowCount}`;
  orderInput.classList.add('create-order');
  orderInput.name = `parent-order-${parentRowCount}`;
  orderInput.required = true;

  const nameLabel = document.createElement('label');
  nameLabel.setAttribute('for', `parent-name-${parentRowCount}`);
  nameLabel.textContent = '要素名';
  const nameInput = document.createElement('input');
  nameInput.type = 'text';
  nameInput.id = `parent-name-${parentRowCount}`;
  nameInput.name = `parent-name-${parentRowCount}`;
  nameInput.required = true;

  const removeButton = document.createElement('button');
  removeButton.type = 'button';
  removeButton.classList.add('remove-button');
  removeButton.textContent = '削除';
  removeButton.onclick = () => removeParentRow(removeButton);

  parentInputGroup.appendChild(orderLabel);
  parentInputGroup.appendChild(orderInput);
  parentInputGroup.appendChild(nameLabel);
  parentInputGroup.appendChild(nameInput);
  parentInputGroup.appendChild(removeButton);

  const childContainer = document.createElement('div');
  childContainer.classList.add('child-container');
  childContainer.dataset.parentIndex = parentRowCount; // 親要素のインデックスをデータ属性に設定

  const addChildButton = document.createElement('button');
  addChildButton.type = 'button';
  addChildButton.classList.add('add-button');
  addChildButton.textContent = '+子要素の追加';
  addChildButton.onclick = () => addChildRow(addChildButton);
  childContainer.appendChild(addChildButton);

  parentRow.appendChild(parentInputGroup);
  parentRow.appendChild(childContainer);

  // 「親要素を追加」ボタンの上で、組み立てたparentRowのブロックを挿入
  const parentButton = document.querySelector('.parent-button');
  parentContainer.insertBefore(parentRow, parentButton);

  childRowCounts[parentRowCount] = 0; // 親要素ごとの子要素カウンタを初期化
  parentRowCount++;
}

function removeParentRow(button) {
  const parentRow = button.closest('.parent-row');
  parentRow.remove();
}

function addChildRow(button) {
  const childContainer = button.closest('.child-container');
  const parentIndex = childContainer.dataset.parentIndex; // 親要素のインデックスを取得
  const childRow = document.createElement('div');
  childRow.classList.add('child-row');

  const childInputGroup = document.createElement('div');
  childInputGroup.classList.add('child-input-group');

  const childOrderLabel = document.createElement('label');
  childOrderLabel.setAttribute('for', `child-order-${parentIndex}-${childRowCounts[parentIndex]}`);
  childOrderLabel.textContent = '作成順';
  const childOrderInput = document.createElement('input');
  childOrderInput.type = 'text';
  childOrderInput.min = '1';
  childOrderInput.max = '999';
  childOrderInput.value = `${childRowCounts[parentIndex] + 1}`;
  childOrderInput.oninput = function() {
    this.value = this.value.replace(/[^0-9]/g, ''); // 半角数字以外の入力を制限
  };
  childOrderInput.classList.add('create-order');
  childOrderInput.id = `child-order-${parentIndex}-${childRowCounts[parentIndex]}`;
  childOrderInput.name = `child-order-${parentIndex}-${childRowCounts[parentIndex]}`;
  childOrderInput.required = true;

  const childNameLabel = document.createElement('label');
  childNameLabel.setAttribute('for', `child-name-${parentIndex}-${childRowCounts[parentIndex]}`);
  childNameLabel.textContent = '要素名';
  const childNameInput = document.createElement('input');
  childNameInput.type = 'text';
  childNameInput.id = `child-name-${parentIndex}-${childRowCounts[parentIndex]}`;
  childNameInput.name = `child-name-${parentIndex}-${childRowCounts[parentIndex]}`;
  childNameInput.required = true;

  const childTagLabel = document.createElement('label');
  childTagLabel.setAttribute('for', `child-tag-${parentIndex}-${childRowCounts[parentIndex]}`);
  childTagLabel.textContent = 'タグ';
  const childTagInput = document.createElement('select');
  childTagInput.id = `child-tag-${parentIndex}-${childRowCounts[parentIndex]}`;
    // 選択肢を生成
    const options = [
      { value: '0', text: '未選択' },
      { value: '1', text: '必須' },
      { value: '2', text: '選択' },
      { value: '3', text: '余力があれば' } 
    ];
    // 選択肢をセレクトボックスに追加
    options.forEach(option => {
      const optionElement = document.createElement('option');
      optionElement.value = option.value;
      optionElement.textContent = option.text;
      // 初期値を「未選択」に設定
      if (option.value === '0') {
        optionElement.selected = true;
      }
      childTagInput.appendChild(optionElement);
    });
  childTagInput.name = `child-tag-${parentIndex}-${childRowCounts[parentIndex]}`;

  const childRemoveButton = document.createElement('button');
  childRemoveButton.type = 'button';
  childRemoveButton.classList.add('remove-button');
  childRemoveButton.textContent = '削除';
  childRemoveButton.onclick = () => removeChildRow(childRemoveButton);

  childInputGroup.appendChild(childOrderLabel);
  childInputGroup.appendChild(childOrderInput);
  childInputGroup.appendChild(childNameLabel);
  childInputGroup.appendChild(childNameInput);
  childInputGroup.appendChild(childTagLabel);
  childInputGroup.appendChild(childTagInput);
  childInputGroup.appendChild(childRemoveButton);

  childRow.appendChild(childInputGroup);
  childContainer.insertBefore(childRow, button);

  childRowCounts[parentIndex]++; // 親要素ごとの子要素カウンタをインクリメント
}

function removeChildRow(button) {
  const childRow = button.closest('.child-row');
  childRow.remove();
}
