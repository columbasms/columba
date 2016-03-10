# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160310175007) do

  create_table "active_admin_comments", force: :cascade do |t|
    t.string   "namespace",     limit: 255
    t.text     "body",          limit: 65535
    t.string   "resource_id",   limit: 255,   null: false
    t.string   "resource_type", limit: 255,   null: false
    t.integer  "author_id",     limit: 4
    t.string   "author_type",   limit: 255
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "active_admin_comments", ["author_type", "author_id"], name: "index_active_admin_comments_on_author_type_and_author_id", using: :btree
  add_index "active_admin_comments", ["namespace"], name: "index_active_admin_comments_on_namespace", using: :btree
  add_index "active_admin_comments", ["resource_type", "resource_id"], name: "index_active_admin_comments_on_resource_type_and_resource_id", using: :btree

  create_table "admin_users", force: :cascade do |t|
    t.string   "email",                  limit: 255,   default: "", null: false
    t.string   "encrypted_password",     limit: 255,   default: "", null: false
    t.string   "reset_password_token",   limit: 255
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          limit: 4,     default: 0,  null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip",     limit: 255
    t.string   "last_sign_in_ip",        limit: 255
    t.datetime "created_at",                                        null: false
    t.datetime "updated_at",                                        null: false
    t.string   "avatar_file_name",       limit: 255
    t.string   "avatar_content_type",    limit: 255
    t.integer  "avatar_file_size",       limit: 4
    t.datetime "avatar_updated_at"
    t.string   "name",                   limit: 255
    t.text     "description",            limit: 65535
  end

  add_index "admin_users", ["email"], name: "index_admin_users_on_email", unique: true, using: :btree
  add_index "admin_users", ["reset_password_token"], name: "index_admin_users_on_reset_password_token", unique: true, using: :btree

  create_table "campaign_addresses", force: :cascade do |t|
    t.string   "address",     limit: 255,                           null: false
    t.datetime "created_at",                                        null: false
    t.datetime "updated_at",                                        null: false
    t.integer  "campaign_id", limit: 4
    t.decimal  "lat",                     precision: 13, scale: 10
    t.decimal  "lng",                     precision: 13, scale: 10
  end

  add_index "campaign_addresses", ["campaign_id"], name: "fk_rails_8063fe85c5", using: :btree

  create_table "campaign_analytics", force: :cascade do |t|
    t.integer  "supporters",  limit: 4
    t.integer  "sent_sms",    limit: 4
    t.datetime "created_at",            null: false
    t.datetime "updated_at",            null: false
    t.integer  "campaign_id", limit: 4
  end

  add_index "campaign_analytics", ["campaign_id"], name: "fk_rails_26fe8d0442", using: :btree

  create_table "campaign_client_receivers", force: :cascade do |t|
    t.datetime "created_at",                                           null: false
    t.datetime "updated_at",                                           null: false
    t.integer  "campaign_id",      limit: 4
    t.integer  "digits_client_id", limit: 4
    t.integer  "receiver_id",      limit: 4
    t.decimal  "lat",                        precision: 13, scale: 10
    t.decimal  "lng",                        precision: 13, scale: 10
  end

  add_index "campaign_client_receivers", ["campaign_id"], name: "fk_rails_0af7050c11", using: :btree
  add_index "campaign_client_receivers", ["digits_client_id"], name: "fk_rails_3c45d07017", using: :btree
  add_index "campaign_client_receivers", ["receiver_id"], name: "fk_rails_c32529c84f", using: :btree

  create_table "campaigns", force: :cascade do |t|
    t.string   "message",            limit: 320,   null: false
    t.datetime "created_at",                       null: false
    t.datetime "updated_at",                       null: false
    t.integer  "organization_id",    limit: 4,     null: false
    t.integer  "town_id",            limit: 4
    t.integer  "province_id",        limit: 4
    t.integer  "region_id",          limit: 4
    t.date     "expires_at",                       null: false
    t.string   "photo_file_name",    limit: 255
    t.string   "photo_content_type", limit: 255
    t.integer  "photo_file_size",    limit: 4
    t.datetime "photo_updated_at"
    t.text     "long_description",   limit: 65535
  end

  add_index "campaigns", ["organization_id"], name: "fk_rails_a74bb03c49", using: :btree
  add_index "campaigns", ["province_id"], name: "fk_rails_9ec4fd6b89", using: :btree
  add_index "campaigns", ["region_id"], name: "fk_rails_4493416bcf", using: :btree
  add_index "campaigns", ["town_id"], name: "fk_rails_a23d672565", using: :btree

  create_table "campaigns_digits_clients", id: false, force: :cascade do |t|
    t.integer "campaign_id",      limit: 4, null: false
    t.integer "digits_client_id", limit: 4, null: false
  end

  add_index "campaigns_digits_clients", ["campaign_id"], name: "index_campaigns_digits_clients_on_campaign_id", using: :btree
  add_index "campaigns_digits_clients", ["digits_client_id"], name: "index_campaigns_digits_clients_on_digits_client_id", using: :btree

  create_table "campaigns_topics", id: false, force: :cascade do |t|
    t.integer "campaign_id", limit: 4, null: false
    t.integer "topic_id",    limit: 4, null: false
  end

  add_index "campaigns_topics", ["campaign_id"], name: "index_campaigns_topics_on_campaign_id", using: :btree
  add_index "campaigns_topics", ["topic_id"], name: "index_campaigns_topics_on_topic_id", using: :btree

  create_table "categories", force: :cascade do |t|
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
    t.string   "title",      limit: 255, null: false
  end

  create_table "digits_clients", force: :cascade do |t|
    t.string   "phone_number",             limit: 255,   null: false
    t.boolean  "enabled"
    t.text     "gcm_token",                limit: 65535, null: false
    t.text     "digits_token",             limit: 65535, null: false
    t.datetime "created_at",                             null: false
    t.datetime "updated_at",                             null: false
    t.text     "digits_secret",            limit: 65535, null: false
    t.string   "id_str",                   limit: 255
    t.string   "digits_verification_type", limit: 255
    t.string   "digits_id",                limit: 255,   null: false
    t.string   "avatar_file_name",         limit: 255
    t.string   "avatar_content_type",      limit: 255
    t.integer  "avatar_file_size",         limit: 4
    t.datetime "avatar_updated_at"
    t.string   "cover_file_name",          limit: 255
    t.string   "cover_content_type",       limit: 255
    t.integer  "cover_file_size",          limit: 4
    t.datetime "cover_updated_at"
    t.string   "user_name",                limit: 255,   null: false
    t.string   "auth_token",               limit: 255,   null: false
    t.integer  "max_sms",                  limit: 4
  end

  create_table "digits_clients_organizations", id: false, force: :cascade do |t|
    t.integer "organization_id",  limit: 4,                 null: false
    t.integer "digits_client_id", limit: 4,                 null: false
    t.boolean "trusted",                    default: false, null: false
  end

  add_index "digits_clients_organizations", ["digits_client_id"], name: "index_digits_clients_organizations_on_digits_client_id", using: :btree
  add_index "digits_clients_organizations", ["organization_id"], name: "index_digits_clients_organizations_on_organization_id", using: :btree

  create_table "digits_clients_topics", id: false, force: :cascade do |t|
    t.integer "digits_client_id", limit: 4, null: false
    t.integer "topic_id",         limit: 4, null: false
  end

  add_index "digits_clients_topics", ["digits_client_id"], name: "index_digits_clients_topics_on_digits_client_id", using: :btree
  add_index "digits_clients_topics", ["topic_id"], name: "index_digits_clients_topics_on_topic_id", using: :btree

  create_table "groups", force: :cascade do |t|
    t.datetime "created_at",                   null: false
    t.datetime "updated_at",                   null: false
    t.string   "name",             limit: 255
    t.integer  "digits_client_id", limit: 4
  end

  add_index "groups", ["digits_client_id"], name: "fk_rails_909b45f4db", using: :btree

  create_table "groups_receivers", id: false, force: :cascade do |t|
    t.integer "group_id",    limit: 4, null: false
    t.integer "receiver_id", limit: 4, null: false
  end

  add_index "groups_receivers", ["group_id"], name: "index_groups_receivers_on_group_id", using: :btree
  add_index "groups_receivers", ["receiver_id"], name: "index_groups_receivers_on_receiver_id", using: :btree

  create_table "organization_analytics", force: :cascade do |t|
    t.integer  "follower",           limit: 4
    t.integer  "truster",            limit: 4
    t.float    "sms_range_general",  limit: 24
    t.float    "sms_range_follower", limit: 24
    t.float    "sms_range_truster",  limit: 24
    t.integer  "global_supporter",   limit: 4
    t.integer  "global_sent_sms",    limit: 4
    t.datetime "created_at",                    null: false
    t.datetime "updated_at",                    null: false
    t.integer  "organization_id",    limit: 4
  end

  add_index "organization_analytics", ["organization_id"], name: "fk_rails_aaf02dc4fd", using: :btree

  create_table "organizations", force: :cascade do |t|
    t.string   "email",                  limit: 255,   default: "",    null: false
    t.string   "encrypted_password",     limit: 255,   default: "",    null: false
    t.string   "reset_password_token",   limit: 255
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          limit: 4,     default: 0,     null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip",     limit: 255
    t.string   "last_sign_in_ip",        limit: 255
    t.datetime "created_at",                                           null: false
    t.datetime "updated_at",                                           null: false
    t.string   "organization_name",      limit: 255,                   null: false
    t.string   "VAT_number",             limit: 255
    t.datetime "locked_at"
    t.string   "logo_file_name",         limit: 255
    t.string   "logo_content_type",      limit: 255
    t.integer  "logo_file_size",         limit: 4
    t.datetime "logo_updated_at"
    t.string   "avatar_file_name",       limit: 255
    t.string   "avatar_content_type",    limit: 255
    t.integer  "avatar_file_size",       limit: 4
    t.datetime "avatar_updated_at"
    t.text     "description",            limit: 65535
    t.string   "cover_file_name",        limit: 255
    t.string   "cover_content_type",     limit: 255
    t.integer  "cover_file_size",        limit: 4
    t.datetime "cover_updated_at"
    t.string   "fiscal_code",            limit: 255,                   null: false
    t.string   "address",                limit: 255,                   null: false
    t.integer  "postal_code",            limit: 4,                     null: false
    t.string   "phone_number",           limit: 255,                   null: false
    t.boolean  "visible",                              default: false, null: false
    t.string   "website",                limit: 255
    t.integer  "town_id",                limit: 4,                     null: false
  end

  add_index "organizations", ["email"], name: "index_organizations_on_email", unique: true, using: :btree
  add_index "organizations", ["reset_password_token"], name: "index_organizations_on_reset_password_token", unique: true, using: :btree
  add_index "organizations", ["town_id"], name: "fk_rails_a4c150a086", using: :btree

  create_table "organizations_topics", id: false, force: :cascade do |t|
    t.integer "organization_id", limit: 4, null: false
    t.integer "topic_id",        limit: 4, null: false
  end

  add_index "organizations_topics", ["organization_id"], name: "index_organizations_topics_on_organization_id", using: :btree
  add_index "organizations_topics", ["topic_id"], name: "index_organizations_topics_on_topic_id", using: :btree

  create_table "post_images", force: :cascade do |t|
    t.datetime "created_at",                    null: false
    t.datetime "updated_at",                    null: false
    t.string   "alt",               limit: 255
    t.string   "hint",              limit: 255
    t.integer  "post_id",           limit: 4
    t.string   "file_file_name",    limit: 255
    t.string   "file_content_type", limit: 255
    t.integer  "file_file_size",    limit: 4
    t.datetime "file_updated_at"
  end

  add_index "post_images", ["post_id"], name: "fk_rails_3ee54b46c4", using: :btree

  create_table "posts", force: :cascade do |t|
    t.string   "title",              limit: 255
    t.text     "content",            limit: 65535
    t.boolean  "published"
    t.datetime "created_at",                       null: false
    t.datetime "updated_at",                       null: false
    t.integer  "admin_user_id",      limit: 4
    t.integer  "category_id",        limit: 4
    t.string   "photo_file_name",    limit: 255
    t.string   "photo_content_type", limit: 255
    t.integer  "photo_file_size",    limit: 4
    t.datetime "photo_updated_at"
  end

  add_index "posts", ["admin_user_id"], name: "fk_rails_c9f8ba6d38", using: :btree
  add_index "posts", ["category_id"], name: "fk_rails_9b1b26f040", using: :btree

  create_table "posts_tags", id: false, force: :cascade do |t|
    t.integer "post_id", limit: 4, null: false
    t.integer "tag_id",  limit: 4, null: false
  end

  add_index "posts_tags", ["post_id"], name: "index_posts_tags_on_post_id", using: :btree
  add_index "posts_tags", ["tag_id"], name: "index_posts_tags_on_tag_id", using: :btree

  create_table "provinces", force: :cascade do |t|
    t.string   "name",       limit: 255, null: false
    t.string   "code",       limit: 255, null: false
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
    t.integer  "region_id",  limit: 4
  end

  add_index "provinces", ["region_id"], name: "fk_rails_5aca3eede1", using: :btree

  create_table "receivers", force: :cascade do |t|
    t.string   "number",      limit: 255,                 null: false
    t.datetime "created_at",                              null: false
    t.datetime "updated_at",                              null: false
    t.boolean  "blacklisted",             default: false, null: false
  end

  create_table "regions", force: :cascade do |t|
    t.string   "name",       limit: 255, null: false
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "shortened_urls", force: :cascade do |t|
    t.integer  "owner_id",   limit: 4
    t.string   "owner_type", limit: 20
    t.text     "url",        limit: 65535,             null: false
    t.string   "unique_key", limit: 10,                null: false
    t.integer  "use_count",  limit: 4,     default: 0, null: false
    t.datetime "expires_at"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "shortened_urls", ["owner_id", "owner_type"], name: "index_shortened_urls_on_owner_id_and_owner_type", using: :btree
  add_index "shortened_urls", ["unique_key"], name: "index_shortened_urls_on_unique_key", unique: true, using: :btree
  add_index "shortened_urls", ["url"], name: "index_shortened_urls_on_url", length: {"url"=>255}, using: :btree

  create_table "tags", force: :cascade do |t|
    t.string   "title",      limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "topic_analytics", force: :cascade do |t|
    t.integer  "followers",  limit: 4
    t.float    "sms_range",  limit: 24
    t.datetime "created_at",            null: false
    t.datetime "updated_at",            null: false
    t.integer  "topic_id",   limit: 4
  end

  add_index "topic_analytics", ["topic_id"], name: "fk_rails_8377fec63d", using: :btree

  create_table "topic_translations", force: :cascade do |t|
    t.integer  "topic_id",    limit: 4,   null: false
    t.string   "locale",      limit: 255, null: false
    t.datetime "created_at",              null: false
    t.datetime "updated_at",              null: false
    t.string   "name",        limit: 255
    t.string   "description", limit: 255
  end

  add_index "topic_translations", ["locale"], name: "index_topic_translations_on_locale", using: :btree
  add_index "topic_translations", ["topic_id"], name: "index_topic_translations_on_topic_id", using: :btree

  create_table "topics", force: :cascade do |t|
    t.string   "name",               limit: 255
    t.text     "description",        limit: 65535
    t.datetime "created_at",                       null: false
    t.datetime "updated_at",                       null: false
    t.string   "main_color",         limit: 255,   null: false
    t.string   "status_color",       limit: 255,   null: false
    t.string   "image_file_name",    limit: 255
    t.string   "image_content_type", limit: 255
    t.integer  "image_file_size",    limit: 4
    t.datetime "image_updated_at"
  end

  create_table "towns", force: :cascade do |t|
    t.string   "name",        limit: 255, null: false
    t.datetime "created_at",              null: false
    t.datetime "updated_at",              null: false
    t.integer  "province_id", limit: 4
  end

  add_index "towns", ["province_id"], name: "fk_rails_9d80790578", using: :btree

  add_foreign_key "campaign_addresses", "campaigns"
  add_foreign_key "campaign_analytics", "campaigns"
  add_foreign_key "campaign_client_receivers", "campaigns"
  add_foreign_key "campaign_client_receivers", "digits_clients"
  add_foreign_key "campaign_client_receivers", "receivers"
  add_foreign_key "campaigns", "organizations"
  add_foreign_key "campaigns", "provinces"
  add_foreign_key "campaigns", "regions"
  add_foreign_key "campaigns", "towns"
  add_foreign_key "groups", "digits_clients"
  add_foreign_key "organization_analytics", "organizations"
  add_foreign_key "organizations", "towns"
  add_foreign_key "post_images", "posts"
  add_foreign_key "posts", "admin_users"
  add_foreign_key "posts", "categories"
  add_foreign_key "provinces", "regions"
  add_foreign_key "topic_analytics", "topics"
  add_foreign_key "towns", "provinces"
end
