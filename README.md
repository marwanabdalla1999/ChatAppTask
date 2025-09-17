# 📱 Chat App – Trial Task (Q1)

### Environment
- **AGP**: 8.6.0  
- **Kotlin**: 1.9.23  
- **minSdk**: 24, **targetSdk**: 34  

---

## 🚨 Issue

After bumping Gradle from **8.5.1 → 8.6.0**, production crashes were reported:

```
java.lang.IllegalStateException: LifecycleOwner is destroyed
    at androidx.lifecycle.LiveData.observe(...)
    at com.app.legacy.ui.ChatFragment.onViewCreated(ChatFragment.kt:112)
```

**Root cause**:  
`LiveData` was being observed with **`requireActivity()`** as the LifecycleOwner.  
When navigating back and forth, the Activity outlives the Fragment, leading to **invalid observer calls on a destroyed Fragment**.

---

## ✅ Minimal Fix (Shipped Today)

Changed **LifecycleOwner** from `requireActivity()` to `viewLifecycleOwner`.

```kotlin
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Fix: tie observer to Fragment’s view lifecycle
        viewModel.messages.observe(viewLifecycleOwner) { msgs ->
            recyclerView.adapter = MessagesAdapter(msgs)
        }
    }
}
```

- **Risk:** Very low → scoped only to this fragment.  
- **Performance:** No extra allocations, observers auto-cleared with the view lifecycle.  

---

## 🧪 Tests

### Unit Tests (JUnit + Mockito)

```kotlin
class ChatViewModelTest {
    private lateinit var viewModel: ChatViewModel

    @Before fun setup() { viewModel = ChatViewModel() }

    @Test fun `messages emits non-empty list`() {
        viewModel.addMessage("Hello")
        assert(viewModel.messages.value?.isNotEmpty() == true)
    }

    @Test fun `messages emits empty initially`() {
        assert(viewModel.messages.value?.isEmpty() == true)
    }
}
```

### Espresso Test

```kotlin
@RunWith(AndroidJUnit4::class)
class ChatFragmentTest {
    @get:Rule
    val scenarioRule = FragmentScenarioRule(ChatFragment::class.java)

    @Test
    fun testMessagesDisplayed() {
        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }
}
```

---

## 🔄 Refactor Plan (Staged with MVVM + Clean)

1. **Domain Layer**: Extract `Message` model + `ChatRepository` interface.  
2. **Data Layer**: Implement repository with local cache + remote API.  
3. **Presentation Layer**: Expose `LiveData`/`StateFlow` of messages via `ChatViewModel`.  
4. **UI Layer**: Use `RecyclerView` with DiffUtil for efficient rendering.  

---

## 🔍 Code Review Notes (For a Junior)

1. **Lifecycle misuse**  
   - ❌ `observe(requireActivity())` → causes crashes.  
   - ✅ Always use `viewLifecycleOwner` in Fragments.  
   - *Explanation*: "The Fragment view can be destroyed while the Activity lives on; we don’t want dead observers firing."

2. **Adapter recreation**  
   - ❌ `recyclerView.adapter = MessagesAdapter(msgs)` every time.  
   - ✅ Use a single adapter instance + `submitList()`.  
   - *Explanation*: "Rebuilding adapters wastes memory and CPU; better to update data inside the existing adapter."

---

## 🚀 Why Not Flow/StateFlow Now?

- **Constraint**: Legacy app with heavy LiveData usage.  
- **Risk**: Large refactor → not suitable for hotfix.  
- **Fix Today**: Stick with LiveData for **low-risk crash resolution**.  
- **Later**: Gradually migrate ViewModel streams to StateFlow in refactor phase.

---

## 📊 Production Metric to Track

- **Crash-free sessions** for `ChatFragment` (Firebase Crashlytics).  
- If stable: expand to **median screen load time** + **battery impact per session**.  

---

✅ All tests pass locally:  
![tests screenshot placeholder](docs/tests-green.png)  
