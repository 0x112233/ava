varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    if (v_color.a == 0) discard;

    vec4 tex_color = texture2D(u_texture, v_texCoords);
    if (tex_color.a == 0)  discard;

    gl_FragColor = v_color * tex_color;
}