import json
import sys


def error(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


def float_to_str(param: float):
    return str(param) + "f" if ('.' in str(param)) else str(param)


def list_to_comma_sep_str(params: list):
    return ', '.join([float_to_str(i) for i in params])


def face_rotation_to_str(angle: int):
    ret = None
    match angle:
        case 0:
            ret = "ZERO"
        case 90:
            ret = "CLOCKWISE_90"
        case 180:
            ret = "UPSIDE_DOWN"
        case 270:
            ret = "COUNTERCLOCKWISE_90"
    return ret


if sys.argv.__len__() == 1:
    error("Missing file parameter")
    exit(-1)

fileName: str = sys.argv[-1]

try:
    with open(fileName, "r") as file:
        model = json.load(file)
except OSError:
    error(f"Failed to open file {fileName}")
    exit(1)

result: str = """
ModelFile model = provider.models().getBuilder(holder.key)
.parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
"""

for element in model["elements"]:
    result += ".element().allFaces((direction, faceBuilder) -> {\n"
    result += "switch(direction) {\n"

    for face, value in element["faces"].items():
        result += f"case {face.upper()} -> faceBuilder"
        result += f".uvs({list_to_comma_sep_str(value['uv'])})"

        if "rotation" in value:
            result += f".rotation(ModelBuilder.FaceRotation.{face_rotation_to_str(value['rotation'])})"

        result += f".texture(\"{value['texture']}\");\n"

    result += "}\n"
    result += "})\n"
    result += f".from({list_to_comma_sep_str(element['from'])}).to({list_to_comma_sep_str(element['to'])})"

    if "rotation" in element and element["rotation"]["angle"] != 0:
        rotation = element["rotation"]

        result += ".rotation()"
        result += f".angle({float_to_str(rotation['angle'])})"
        result += f".axis(Direction.Axis.{rotation['axis'].upper()})"
        result += f".origin({list_to_comma_sep_str(rotation['origin'])})"
        result += ".end()\n"
    else:
        result += "\n"

    result += ".end()\n"

print(result)
