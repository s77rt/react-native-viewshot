# @s77rt/react-native-viewshot

A React Native module to capture views.

## Installation

```bash
npm install @s77rt/react-native-viewshot
```

## Usage

Attach ref to `View` and pass `collapsable={false}`

```jsx
<View
  ref={viewRef}
  collapsable={false} // Important to ensure existence in native view hierarchy
/>
```

Use `Viewshot`

```jsx
import { findNodeHandle } from "react-native";
import Viewshot from "@s77rt/react-native-viewshot";

const captureView = useCallback(() => {
  const nodeHandle = findNodeHandle(viewRef.current);
  if (!nodeHandle) {
    return;
  }

  Viewshot.capture(nodeHandle)
    .then((uri) => {
      console.log(uri);
    })
    .catch((error) => {
      console.error(error);
    });
}, []);
```

## Methods

|   Name    |      Arguments       |                                                        Description                                                        |
| :-------: | :------------------: | :-----------------------------------------------------------------------------------------------------------------------: |
| `capture` | `nodeHandle: number` | Capture a view given its handle/reactTag. Returns a `Promise<string>` with the file uri. **Note:** File is saved as PNG. |

## License

[MIT](LICENSE)
