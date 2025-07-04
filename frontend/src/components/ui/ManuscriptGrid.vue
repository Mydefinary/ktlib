<template>
<v-container>
<!-- 등록/수정 버튼 -->
<div style="margin-bottom: 16px;">
<v-btn color="primary" @click="openAdd">등록</v-btn>
<v-btn color="primary" :disabled="!selected" @click="openEdit">수정</v-btn>
</div>
 
    <!-- 테이블 -->
<v-table>
<thead>
<tr>
<th>ID</th>
<th>제목</th>
<th>내용</th>
</tr>
</thead>
<tbody>
<tr
          v-for="(item, index) in manuscripts"
          :key="index"
          :style="item === selected ? 'background: #eef;' : ''"
          @click="selected = item"
>
<td>{{ index + 1 }}</td>
<td>{{ item.title }}</td>
<td>{{ item.content.length > 100 ? item.content.slice(0, 100) + '...' : item.content }}</td>
</tr>
</tbody>
</v-table>
 
    <!-- 등록 다이얼로그 -->
<v-dialog v-model="showAdd" width="500">
<v-card>
<v-card-title>원고 등록</v-card-title>
<v-card-text>
<Manuscript :initial-value="emptyManuscript()" :isNew="true" @add="addManuscript" />
</v-card-text>
</v-card>
</v-dialog>
 
    <!-- 수정 다이얼로그 -->
<v-dialog v-model="showEdit" width="500">
<v-card>
<v-card-title>원고 수정</v-card-title>
<v-card-text>
<Manuscript :initial-value="selected" :isNew="false" @save="updateManuscript" />
</v-card-text>
</v-card>
</v-dialog>
</v-container>
</template>
 
<script>
import Manuscript from './Manuscript.vue'
 
export default {
  components: { Manuscript },
  data() {
    return {
      manuscripts: [],
      selected: null,
      showAdd: false,
      showEdit: false,
    }
  },
  methods: {
    openAdd() {
      this.showAdd = true
    },
    openEdit() {
      this.showEdit = true
    },
    addManuscript(newItem) {
      this.manuscripts.push(newItem)
      this.showAdd = false
    },
    updateManuscript(updatedItem) {
      const index = this.manuscripts.indexOf(this.selected)
      if (index !== -1) {
        this.manuscripts.splice(index, 1, updatedItem)
        this.selected = updatedItem
      }
      this.showEdit = false
    },
    emptyManuscript() {
      return {
        title: '',
        content: '',
        createdDate: new Date().toISOString(),
        lastModified: null,
        status: 'DRAFT'
      }
    }
  }
}
</script>