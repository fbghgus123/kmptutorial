# Todo 앱 구현 완료

## 📁 프로젝트 구조

클린 아키텍처와 기능 단위 모듈화로 구성되었습니다:

```
todo/
├── domain/              # 비즈니스 로직 레이어
│   ├── model/
│   │   └── Todo.kt      # Todo 엔티티
│   ├── repository/
│   │   └── TodoRepository.kt  # 레포지토리 인터페이스
│   └── usecase/         # 비즈니스 유스케이스들
│       ├── GetTodosUseCase.kt
│       ├── AddTodoUseCase.kt
│       ├── UpdateTodoUseCase.kt
│       ├── DeleteTodoUseCase.kt
│       └── ToggleTodoUseCase.kt
│
├── data/                # 데이터 레이어
│   ├── datasource/
│   │   └── TodoDataSource.kt  # 인메모리 데이터 소스
│   └── repository/
│       └── TodoRepositoryImpl.kt  # 레포지토리 구현
│
├── presentation/        # 프레젠테이션 레이어
│   ├── TodoUiState.kt   # UI 상태
│   ├── TodoViewModel.kt # ViewModel
│   └── ui/
│       └── TodoScreen.kt  # Compose UI
│
└── di/                  # 의존성 주입
    └── TodoModule.kt    # 간단한 DI 컨테이너
```

## ✨ 주요 기능

1. **할 일 추가** - 제목과 설명(선택)을 입력하여 새 할 일 생성
2. **할 일 완료/미완료 토글** - 체크박스를 통한 상태 변경
3. **할 일 삭제** - 삭제 확인 다이얼로그와 함께 제공
4. **필터링** - 전체/진행중/완료 필터로 할 일 목록 필터링
5. **카운터** - 진행중 및 완료된 할 일 개수 표시

## 🎨 UI 특징

- Material Design 3 사용
- 모던한 카드 디자인
- 완료된 항목에 취소선 효과
- 빈 상태에 대한 이모지 기반 안내 메시지
- 스무스한 애니메이션 효과
- FloatingActionButton으로 직관적인 할 일 추가

## 🏗️ 아키텍처 특징

### 클린 아키텍처 3계층
- **Domain Layer**: 비즈니스 로직과 엔티티 (의존성 없음)
- **Data Layer**: 데이터 소스 및 레포지토리 구현
- **Presentation Layer**: UI 상태 관리 및 화면

### 의존성 방향
```
Presentation → Domain ← Data
```

### 데이터 저장
- MutableStateFlow를 사용한 인메모리 저장
- Flow를 통한 반응형 데이터 스트림
- 앱 재시작 시 데이터 초기화

## 🚀 실행 방법

### Android
```bash
./gradlew :composeApp:installDebug
```

### iOS
Xcode에서 iosApp 프로젝트를 열어 실행

## 📝 향후 확장 가능 사항

1. **영속성 추가** - Room, SQLDelight 등으로 데이터 영구 저장
2. **정렬 기능** - 날짜, 제목, 중요도별 정렬
3. **우선순위** - 할 일에 우선순위 레벨 추가
4. **카테고리** - 할 일을 카테고리별로 분류
5. **검색 기능** - 제목과 설명으로 검색
6. **수정 기능** - 기존 할 일 수정 UI
7. **알림 기능** - 푸시 알림 및 리마인더
