<template>
  <v-data-table
    :headers="headers"
    :items="items"
    :items-per-page="5"
    class="elevation-1"
  >
    <!-- ✅ 커버 이미지 렌더링 -->
    <template #item.coverUrl="{ item }">
      <v-img
        :src="item.coverUrl"
        width="80"
        height="120"
        class="ma-2 rounded"
        cover
      />
    </template>

    <!-- ✅ 날짜 포맷 -->
    <template #item.registrationDate="{ item }">
      {{ formatDate(item.registrationDate) }}
    </template>
    <template #item.publicationDate="{ item }">
      {{ formatDate(item.publicationDate) }}
    </template>

    <!-- ✅ 긴 내용 자르기 -->
    <template #item.content="{ item }">
      {{ truncate(item.content, 20) }}
    </template>
    <template #item.summary="{ item }">
      {{ truncate(item.summary, 50) }}
    </template>
    <template #item.genre="{ item }">
      {{ truncate(item.genre, 20) }}
    </template>

    <!-- ✅ PDF 버튼 -->
    <template #item.action="{ item }">
      <v-btn
        color="primary"
        size="small"
        @click="downloadPdf(item.bookId)"
      >
        PDF 보기
      </v-btn>
    </template>
  </v-data-table>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { VDataTable } from 'vuetify/labs/VDataTable';

export default {
  name: 'BookListView',
  components: { VDataTable },
  props: {
    value: Object,
    editMode: Boolean,
    isNew: Boolean
  },
  setup() {
    const headers = ref([
      { title: "Book ID", key: "bookId", sortable: true },
      { title: "표지", key: "coverUrl" },
      { title: "제목", key: "title" },
      { title: "작가필명", key: "authorNickname" },
      { title: "내용", key: "content" },
      { title: "장르", key: "genre" },
      { title: "요약", key: "summary" },
      { title: "열람료", key: "readCost" },
      { title: "출간일", key: "publicationDate" },
      { title: "구독자 수", key: "numberOfSubscribers" },
      { title: "PDF", key: "action", sortable: false },  // ✅ PDF 버튼 헤더 추가
    ]);

    const items = ref([]);

    onMounted(async () => {
      try {
        const response = await axios.get('/bookLists');
        const data = response.data._embedded.bookLists;

        data.forEach(obj => {
          obj.bookId = Number(obj._links.self.href.split("/").pop());
        });

        items.value = data;
      } catch (error) {
        console.error('📛 Error fetching book list:', error);
      }
    });

    const formatDate = (isoString) => {
      if (!isoString) return '-';
      const date = new Date(isoString);
      return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    };

    const truncate = (text, len = 100) => {
      if (!text) return '';
      return text.length > len ? text.slice(0, len) + '...' : text;
    };

    // ✅ PDF 다운로드 함수
    const downloadPdf = async (bookId) => {
      try {
        const res = await axios.get(`/books/${bookId}/pdf`);
        const pdfUrl = res.data;

        // 새 창으로 열기 또는 자동 다운로드
        window.open(pdfUrl, '_blank');
      } catch (err) {
        console.error('❌ PDF 생성 실패:', err);
        alert('PDF 생성 실패');
      }
    };

    return {
      headers,
      items,
      formatDate,
      truncate,
      downloadPdf,
    };
  }
};
</script>

<style scoped>
.v-img {
  border-radius: 4px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}
</style>
